package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.DeleteTransferUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Transfer;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransferRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class DeleteTransferUseCaseImpl implements DeleteTransferUseCase {

    private final TransferRepository transferRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public DeleteTransferUseCaseImpl(TransferRepository transferRepository, TransactionRepository transactionRepository,
            WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public void execute(UUID transactionId, UUID userId) {
        Transfer transfer = transferRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transferência não encontrada"));

        if (!transfer.getUserId().equals(userId)) {
            throw new ForbiddenException("Você não tem permissão para excluir esta transferência");
        }

        Wallet originWallet = walletRepository.findById(transfer.getFromWalletId())
                .orElseThrow(() -> new IllegalStateException("Carteira de origem não encontrada"));

        Wallet destinationWallet = walletRepository.findById(transfer.getToWalletId())
                .orElseThrow(() -> new IllegalStateException("Carteira de destino não encontrada"));

        // Reverte saldos
        originWallet.addFunds(transfer.getAmount());
        destinationWallet.removeFunds(transfer.getAmount());

        walletRepository.save(originWallet);
        walletRepository.save(destinationWallet);

        transactionRepository.deleteById(transfer.getTransactionOutId());
        transactionRepository.deleteById(transfer.getTransactionInId());
        transferRepository.deleteById(transfer.getId());
    }
}
