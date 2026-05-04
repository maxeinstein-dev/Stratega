package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.CreateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.usecase.CreateTransactionUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;

public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public CreateTransactionUseCaseImpl(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public TransactionResponse execute(CreateTransactionRequest request) {
        Wallet wallet = walletRepository.findById(request.walletId())
                .orElseThrow(() -> new IllegalArgumentException("Carteira não encontrada"));

        Transaction transaction = new Transaction(
                null,
                request.description(),
                request.amount(),
                request.date(),
                request.type(),
                request.walletId(),
                request.categoryId(),
                null
        );

        if (transaction.isIncome()) {
            wallet.addFunds(transaction.getAmount());
        } else if (transaction.isExpense()) {
            wallet.removeFunds(transaction.getAmount());
        }

        walletRepository.save(wallet);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getDescription(),
                savedTransaction.getAmount(),
                savedTransaction.getDate(),
                savedTransaction.getType(),
                savedTransaction.getWalletId(),
                savedTransaction.getCategoryId(),
                savedTransaction.getLinkedTransactionId()
        );
    }
}
