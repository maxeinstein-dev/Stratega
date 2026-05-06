package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.CreateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.usecase.CreateTransactionUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;

import br.com.maxsueleinstein.stratega.domain.event.BudgetExceededEvent;
import br.com.maxsueleinstein.stratega.domain.model.Budget;
import br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CreateTransactionUseCaseImpl(TransactionRepository transactionRepository, 
                                      WalletRepository walletRepository,
                                      BudgetRepository budgetRepository,
                                      CategoryRepository categoryRepository,
                                      ApplicationEventPublisher eventPublisher) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
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
                // Detectar overdraft: se carteira restrita e operação causaria saldo negativo,
                // converter automaticamente e disparar evento de notificação
                if (!wallet.isAllowNegativeBalance() && wallet.wouldGoNegative(transaction.getAmount())) {
                    wallet.enableOverdraft();
                    eventPublisher.publishEvent(new br.com.maxsueleinstein.stratega.domain.event.OverdraftActivatedEvent(
                            wallet.getUserId(),
                            wallet.getName(),
                            wallet.getBalance(),
                            transaction.getAmount()
                    ));
                }
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
                    savedTransaction.getLinkedTransactionId(),
                    savedTransaction.getGroupId()
            ));
        }

        walletRepository.save(wallet);

        // Check budget for expenses with category outside the loop to avoid N+1
        if (request.type() == br.com.maxsueleinstein.stratega.domain.model.TransactionType.EXPENSE && request.categoryId() != null) {
            java.util.Set<String> checkedPeriods = new java.util.HashSet<>();
            for (TransactionResponse resp : responses) {
                int m = resp.date().getMonthValue();
                int y = resp.date().getYear();
                String key = m + "-" + y;
                if (checkedPeriods.add(key)) {
                    // We only need a temporary transaction to pass the date/category to checkBudgetExceeded
                    Transaction temp = new Transaction(null, "", java.math.BigDecimal.ZERO, resp.date(), request.type(), request.walletId(), request.categoryId(), null);
                    checkBudgetExceeded(temp, wallet.getUserId());
                }
            }
        }

        return responses;
    }

    private void checkBudgetExceeded(Transaction transaction, java.util.UUID userId) {
        int month = transaction.getDate().getMonthValue();
        int year = transaction.getDate().getYear();

        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
                userId, transaction.getCategoryId(), month, year);

        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            
            // Calculate current spent for this category/month/year
            java.math.BigDecimal currentSpent = transactionRepository.findByUserId(userId).stream()
                    .filter(tx -> tx.isExpense() && 
                                  tx.getCategoryId() != null && 
                                  tx.getCategoryId().equals(transaction.getCategoryId()) &&
                                  tx.getDate().getMonthValue() == month &&
                                  tx.getDate().getYear() == year)
                    .map(Transaction::getEffectiveAmount)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

            if (currentSpent.compareTo(budget.getAmountLimit()) > 0) {
                String categoryName = categoryRepository.findById(transaction.getCategoryId())
                        .map(br.com.maxsueleinstein.stratega.domain.model.Category::getName)
                        .orElse("Categoria");
                
                eventPublisher.publishEvent(new BudgetExceededEvent(
                        userId,
                        categoryName,
                        budget.getAmountLimit(),
                        currentSpent
                ));
            }
        }
    }
}
