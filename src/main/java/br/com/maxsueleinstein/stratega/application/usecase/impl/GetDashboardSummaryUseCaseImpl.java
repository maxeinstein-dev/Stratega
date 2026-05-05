package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.DashboardSummaryResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetDashboardSummaryUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.model.Currency;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetDashboardSummaryUseCaseImpl implements GetDashboardSummaryUseCase {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private final ExchangeRateService exchangeRateService;

    public GetDashboardSummaryUseCaseImpl(TransactionRepository transactionRepository, 
                                        CategoryRepository categoryRepository,
                                        WalletRepository walletRepository,
                                        ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public DashboardSummaryResponse execute(UUID userId, Integer month, Integer year) {
        // Map to cache wallet currencies for this execution
        Map<UUID, Currency> walletCurrencies = walletRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(Wallet::getId, Wallet::getCurrency));

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
        Map<String, BigDecimal> expensesByCategory = new HashMap<>();

        for (Transaction tx : transactions) {
            Currency txCurrency = walletCurrencies.getOrDefault(tx.getWalletId(), Currency.BRL);
            BigDecimal amountBrl = exchangeRateService.convert(tx.getEffectiveAmount(), txCurrency, Currency.BRL);

            if (tx.isIncome()) {
                totalIncome = totalIncome.add(amountBrl);
            } else if (tx.isExpense()) {
                totalExpense = totalExpense.add(amountBrl);
                
                if (tx.getCategoryId() != null) {
                    String categoryName = categoryRepository.findById(tx.getCategoryId())
                            .map(Category::getName)
                            .orElse("Desconhecida");
                    expensesByCategory.merge(categoryName, amountBrl, BigDecimal::add);
                }
            }
        }

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new DashboardSummaryResponse(totalIncome, totalExpense, balance, expensesByCategory);
    }
}
