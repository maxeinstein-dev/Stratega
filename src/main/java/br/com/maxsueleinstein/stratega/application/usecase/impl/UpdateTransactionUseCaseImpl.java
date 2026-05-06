package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.dto.UpdateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.usecase.UpdateTransactionUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;

import java.util.UUID;

public class UpdateTransactionUseCaseImpl implements UpdateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public UpdateTransactionUseCaseImpl(TransactionRepository transactionRepository,
            WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public TransactionResponse execute(UUID transactionId, UUID requesterId, UpdateTransactionRequest request) {
        Transaction primaryTx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        Wallet oldPrimaryWallet = walletRepository.findById(primaryTx.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Carteira antiga da transação não encontrada"));

        if (!oldPrimaryWallet.getUserId().equals(requesterId)) {
            throw new ForbiddenException("Você não tem permissão para editar transações desta carteira");
        }

        Wallet newPrimaryWallet = oldPrimaryWallet;
        if (!primaryTx.getWalletId().equals(request.walletId())) {
            newPrimaryWallet = walletRepository.findById(request.walletId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nova carteira não encontrada"));
            if (!newPrimaryWallet.getUserId().equals(requesterId)) {
                throw new ForbiddenException("Você não tem permissão para usar esta nova carteira");
            }
        }

        if (primaryTx.getLinkedTransactionId() != null) {
            return updateTransfer(primaryTx, oldPrimaryWallet, newPrimaryWallet, request);
        } else {
            return updateSingleTransaction(primaryTx, oldPrimaryWallet, newPrimaryWallet, request);
        }
    }

    private TransactionResponse updateSingleTransaction(Transaction tx, Wallet oldWallet, Wallet newWallet,
            UpdateTransactionRequest request) {
        // Revert old impact
        revertTransactionImpact(tx, oldWallet);

        // Update details
        tx.updateDetails(request.description(), request.amount(), request.date(), request.categoryId(),
                request.walletId());

        // Apply new impact
        applyTransactionImpact(tx, newWallet);

        walletRepository.save(oldWallet);
        if (!oldWallet.getId().equals(newWallet.getId())) {
            walletRepository.save(newWallet);
        }

        Transaction savedTx = transactionRepository.save(tx);
        return toResponse(savedTx);
    }

    private TransactionResponse updateTransfer(Transaction primaryTx, Wallet oldPrimaryWallet, Wallet newPrimaryWallet,
            UpdateTransactionRequest request) {
        Transaction linkedTx = transactionRepository.findById(primaryTx.getLinkedTransactionId())
                .orElseThrow(() -> new IllegalStateException("Transação vinculada não encontrada"));

        Wallet oldLinkedWallet = walletRepository.findById(linkedTx.getWalletId())
                .orElseThrow(() -> new IllegalStateException("Carteira da transação vinculada não encontrada"));

        // Revert both impacts
        revertTransactionImpact(primaryTx, oldPrimaryWallet);
        revertTransactionImpact(linkedTx, oldLinkedWallet);

        // Update primary transaction
        primaryTx.updateDetails(request.description(), request.amount(), request.date(), request.categoryId(),
                request.walletId());

        // Update linked transaction (mirrored)
        // Keep its original wallet ID unless we wanted to allow editing BOTH wallets at
        // once, but this request only provides one walletId.
        // So the request.walletId() applies to the primary transaction. The linked
        // transaction keeps its current walletId.
        linkedTx.updateDetails(request.description(), request.amount(), request.date(), request.categoryId(),
                linkedTx.getWalletId());

        // Apply new impacts
        applyTransactionImpact(primaryTx, newPrimaryWallet);
        applyTransactionImpact(linkedTx, oldLinkedWallet); // linked transaction wallet didn't change

        walletRepository.save(oldPrimaryWallet);
        if (!oldPrimaryWallet.getId().equals(newPrimaryWallet.getId())) {
            walletRepository.save(newPrimaryWallet);
        }

        // Save the oldLinkedWallet (if it is distinct from the primary wallets, which
        // it usually is for a transfer)
        if (!oldLinkedWallet.getId().equals(oldPrimaryWallet.getId())
                && !oldLinkedWallet.getId().equals(newPrimaryWallet.getId())) {
            walletRepository.save(oldLinkedWallet);
        } else if (oldLinkedWallet.getId().equals(oldPrimaryWallet.getId())) {
            walletRepository.save(oldPrimaryWallet); // re-save just in case
        } else if (oldLinkedWallet.getId().equals(newPrimaryWallet.getId())) {
            walletRepository.save(newPrimaryWallet); // re-save just in case
        }

        transactionRepository.save(linkedTx);
        Transaction savedPrimaryTx = transactionRepository.save(primaryTx);

        return toResponse(savedPrimaryTx);
    }

    private void revertTransactionImpact(Transaction tx, Wallet wallet) {
        if (tx.isIncome()) {
            wallet.removeFunds(tx.getAmount());
        } else if (tx.isExpense()) {
            wallet.addFunds(tx.getAmount());
        }
    }

    private void applyTransactionImpact(Transaction tx, Wallet wallet) {
        if (tx.isIncome()) {
            wallet.addFunds(tx.getAmount());
        } else if (tx.isExpense()) {
            wallet.removeFunds(tx.getAmount());
        }
    }

    private TransactionResponse toResponse(Transaction savedTransaction) {
        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getDescription(),
                savedTransaction.getAmount(),
                savedTransaction.getNetAmount(),
                savedTransaction.getDate(),
                savedTransaction.getType(),
                savedTransaction.getWalletId(),
                savedTransaction.getCategoryId(),
                savedTransaction.getLinkedTransactionId(),
                savedTransaction.getGroupId());
    }
}
