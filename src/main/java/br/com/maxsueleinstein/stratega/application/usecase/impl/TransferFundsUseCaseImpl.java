package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.TransferFundsRequest;
import br.com.maxsueleinstein.stratega.application.usecase.TransferFundsUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.TransactionType;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;

import java.util.UUID;

public class TransferFundsUseCaseImpl implements TransferFundsUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final br.com.maxsueleinstein.stratega.domain.repository.TransferRepository transferRepository;

    public TransferFundsUseCaseImpl(WalletRepository walletRepository, TransactionRepository transactionRepository, br.com.maxsueleinstein.stratega.domain.repository.TransferRepository transferRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.transferRepository = transferRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void execute(TransferFundsRequest request) {
        if (request.originWalletId().equals(request.destinationWalletId())) {
            throw new IllegalArgumentException("As carteiras de origem e destino devem ser diferentes");
        }

        Wallet originWallet = walletRepository.findById(request.originWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Carteira de origem não encontrada"));
        
        Wallet destinationWallet = walletRepository.findById(request.destinationWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Carteira de destino não encontrada"));

        // Domain rule for removing funds verifies balance and throws exception if insufficient
        originWallet.removeFunds(request.amount());
        destinationWallet.addFunds(request.amount());

        UUID outTransactionId = UUID.randomUUID();
        UUID inTransactionId = UUID.randomUUID();

        Transaction outTransaction = new Transaction(
                outTransactionId,
                request.description(),
                request.amount(),
                null, // netAmount
                request.date(),
                TransactionType.TRANSFER_OUT,
                request.originWalletId(),
                request.categoryId(),
                inTransactionId
        );

        Transaction inTransaction = new Transaction(
                inTransactionId,
                request.description(),
                request.amount(),
                null, // netAmount
                request.date(),
                TransactionType.TRANSFER_IN,
                request.destinationWalletId(),
                request.categoryId(),
                outTransactionId
        );

        br.com.maxsueleinstein.stratega.domain.model.Transfer transfer = new br.com.maxsueleinstein.stratega.domain.model.Transfer(
                UUID.randomUUID(),
                originWallet.getUserId(),
                request.originWalletId(),
                request.destinationWalletId(),
                request.amount(),
                request.description(),
                request.date(),
                outTransactionId,
                inTransactionId
        );

        walletRepository.save(originWallet);
        walletRepository.save(destinationWallet);
        transactionRepository.save(outTransaction);
        transactionRepository.save(inTransaction);
        transferRepository.save(transfer);
    }
}
