package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.BudgetResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetBudgetsUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Budget;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetBudgetsUseCaseImpl implements GetBudgetsUseCase {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public GetBudgetsUseCaseImpl(BudgetRepository budgetRepository, TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<BudgetResponse> execute(UUID userId, int month, int year) {
        List<Budget> budgets = budgetRepository.findByUserIdAndMonthAndYear(userId, month, year);

        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(tx -> tx.isExpense() && tx.getCategoryId() != null)
                .filter(tx -> tx.getDate() != null && tx.getDate().getMonthValue() == month && tx.getDate().getYear() == year)
                .collect(Collectors.toList());

        Map<UUID, BigDecimal> spentByCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategoryId,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getEffectiveAmount, BigDecimal::add)
                ));

        return budgets.stream().map(budget -> {
            BigDecimal currentSpent = spentByCategory.getOrDefault(budget.getCategoryId(), BigDecimal.ZERO);
            BigDecimal percentageUsed = BigDecimal.ZERO;

            if (budget.getAmountLimit().compareTo(BigDecimal.ZERO) > 0) {
                percentageUsed = currentSpent.multiply(new BigDecimal("100"))
                        .divide(budget.getAmountLimit(), 2, RoundingMode.HALF_UP);
            }

            boolean isOverBudget = percentageUsed.compareTo(new BigDecimal("100")) >= 0;

            String categoryName = categoryRepository.findById(budget.getCategoryId())
                    .map(Category::getName)
                    .orElse("Desconhecida");

            return new BudgetResponse(
                    budget.getId(),
                    budget.getCategoryId(),
                    categoryName,
                    budget.getAmountLimit(),
                    currentSpent,
                    percentageUsed,
                    isOverBudget,
                    budget.getMonth(),
                    budget.getYear()
            );
        }).collect(Collectors.toList());
    }
}
