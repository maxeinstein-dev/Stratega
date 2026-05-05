package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.DashboardSummaryResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetDashboardSummaryUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetDashboardSummaryUseCaseImpl implements GetDashboardSummaryUseCase {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public GetDashboardSummaryUseCaseImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public DashboardSummaryResponse execute(UUID userId, Integer month, Integer year) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(tx -> {
                    if (month == null && year == null) return true;
                    if (tx.getDate() == null) return false;
                    boolean matchMonth = month == null || tx.getDate().getMonthValue() == month;
                    boolean matchYear = year == null || tx.getDate().getYear() == year;
                    return matchMonth && matchYear;
                })
                .collect(Collectors.toList());

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Transaction tx : transactions) {
            if (tx.isIncome()) {
                totalIncome = totalIncome.add(tx.getEffectiveAmount());
            } else if (tx.isExpense()) {
                totalExpense = totalExpense.add(tx.getEffectiveAmount());
            }
        }

        BigDecimal balance = totalIncome.subtract(totalExpense);

        Map<String, BigDecimal> expensesByCategory = transactions.stream()
                .filter(Transaction::isExpense)
                .filter(tx -> tx.getCategoryId() != null)
                .collect(Collectors.groupingBy(
                        tx -> categoryRepository.findById(tx.getCategoryId())
                                .map(Category::getName)
                                .orElse("Desconhecida"),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getEffectiveAmount, BigDecimal::add)
                ));

        return new DashboardSummaryResponse(totalIncome, totalExpense, balance, expensesByCategory);
    }
}
