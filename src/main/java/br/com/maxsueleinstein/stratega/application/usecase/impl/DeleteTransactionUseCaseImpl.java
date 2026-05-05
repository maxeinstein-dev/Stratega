package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.DeleteTransactionUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class DeleteTransactionUseCaseImpl implements DeleteTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final br.com.maxsueleinstein.stratega.application.usecase.DeleteTransferUseCase deleteTransferUseCase;

    public DeleteTransactionUseCaseImpl(TransactionRepository transactionRepository, WalletRepository walletRepository, br.com.maxsueleinstein.stratega.application.usecase.DeleteTransferUseCase deleteTransferUseCase) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.deleteTransferUseCase = deleteTransferUseCase;
    }

    @Override
    @Transactional
    public void execute(UUID transactionId, UUID userId) {
        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        // Handle Transfer
        if (tx.getType().name().startsWith("TRANSFER")) {
            deleteTransferUseCase.execute(transactionId, userId);
            return;
        }

        Wallet wallet = walletRepository.findById(tx.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));

        if (!wallet.getUserId().equals(userId)) {
            throw new ForbiddenException("Você não tem permissão para excluir esta transação");
        }

        deleteSingleTransaction(tx, wallet);
    }

    private void deleteSingleTransaction(Transaction tx, Wallet wallet) {
        revertImpact(tx, wallet);
        walletRepository.save(wallet);
        transactionRepository.deleteById(tx.getId());
    }

    private void revertImpact(Transaction tx, Wallet wallet) {
        if (tx.isIncome()) {
            wallet.removeFunds(tx.getAmount());
        } else if (tx.isExpense()) {
            wallet.addFunds(tx.getAmount());
        }
    }
}
