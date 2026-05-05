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
    public java.util.List<TransactionResponse> execute(CreateTransactionRequest request) {
        Wallet wallet = walletRepository.findById(request.walletId())
                .orElseThrow(() -> new IllegalArgumentException("Carteira não encontrada"));

        int loops = 1;
        boolean isInstallment = false;
        java.math.BigDecimal amountPerLoop = request.amount();

        if (request.installments() != null && request.installments() > 1) {
            loops = request.installments();
            isInstallment = true;
            amountPerLoop = request.amount().divide(new java.math.BigDecimal(loops), java.math.RoundingMode.HALF_UP);
        } else if (request.recurringMonths() != null && request.recurringMonths() > 1) {
            loops = request.recurringMonths();
        }

        java.util.List<TransactionResponse> responses = new java.util.ArrayList<>();
        java.time.LocalDateTime currentDate = request.date() != null ? request.date() : java.time.LocalDateTime.now();

        for (int i = 0; i < loops; i++) {
            String description = request.description();
            if (isInstallment) {
                description += " (" + (i + 1) + "/" + loops + ")";
            }

            Transaction transaction = new Transaction(
                    null,
                    description,
                    amountPerLoop,
                    currentDate.plusMonths(i),
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

            Transaction savedTransaction = transactionRepository.save(transaction);

            responses.add(new TransactionResponse(
                    savedTransaction.getId(),
                    savedTransaction.getDescription(),
                    savedTransaction.getAmount(),
                    savedTransaction.getNetAmount(),
                    savedTransaction.getDate(),
                    savedTransaction.getType(),
                    savedTransaction.getWalletId(),
                    savedTransaction.getCategoryId(),
                    savedTransaction.getLinkedTransactionId()
            ));
        }

        walletRepository.save(wallet);
        return responses;
    }
}
