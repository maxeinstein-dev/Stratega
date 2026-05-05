package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.DeleteWalletUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;

import java.util.UUID;

public class DeleteWalletUseCaseImpl implements DeleteWalletUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public DeleteWalletUseCaseImpl(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void execute(UUID walletId, UUID userId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));

        if (!wallet.getUserId().equals(userId)) {
            throw new ForbiddenException("Você não tem permissão para excluir esta carteira");
        }

        boolean hasTransactions = transactionRepository.existsByWalletId(walletId);

        if (hasTransactions) {
            // Soft delete (arquivamento)
            wallet.archive();
            walletRepository.save(wallet);
        } else {
            // Hard delete
            walletRepository.deleteById(walletId);
        }
    }
}
