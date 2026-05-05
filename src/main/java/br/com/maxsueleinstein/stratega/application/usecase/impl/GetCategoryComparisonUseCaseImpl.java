package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.GetCategoryComparisonUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.model.Currency;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetCategoryComparisonUseCaseImpl implements GetCategoryComparisonUseCase {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private final ExchangeRateService exchangeRateService;

    public GetCategoryComparisonUseCaseImpl(TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            WalletRepository walletRepository,
            ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public Map<String, ComparisonData> execute(UUID userId, int month, int year) {
        LocalDate currentMonthStart = LocalDate.of(year, month, 1);
        LocalDate previousMonthStart = currentMonthStart.minusMonths(1);

        Map<UUID, Currency> walletCurrencies = walletRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(Wallet::getId, Wallet::getCurrency));

        Map<String, BigDecimal> currentTotals = calculateTotalsForMonth(userId, currentMonthStart, walletCurrencies);
        Map<String, BigDecimal> previousTotals = calculateTotalsForMonth(userId, previousMonthStart, walletCurrencies);

        Map<String, ComparisonData> result = new HashMap<>();

        currentTotals.forEach((category, currentAmount) -> {
            BigDecimal previousAmount = previousTotals.getOrDefault(category, BigDecimal.ZERO);
            BigDecimal diffPercent = calculateDifference(currentAmount, previousAmount);
            result.put(category, new ComparisonData(currentAmount, previousAmount, diffPercent));
        });

        // Add categories that were in previous month but not in current
        previousTotals.forEach((category, previousAmount) -> {
            if (!result.containsKey(category)) {
                result.put(category, new ComparisonData(BigDecimal.ZERO, previousAmount, new BigDecimal("-100")));
            }
        });

        return result;
    }

    private Map<String, BigDecimal> calculateTotalsForMonth(UUID userId, LocalDate start,
            Map<UUID, Currency> walletCurrencies) {
        int month = start.getMonthValue();
        int year = start.getYear();

        return transactionRepository.findByUserId(userId).stream()
                .filter(tx -> tx.isExpense() &&
                        tx.getDate().getMonthValue() == month &&
                        tx.getDate().getYear() == year &&
                        tx.getCategoryId() != null)
                .collect(Collectors.groupingBy(
                        tx -> categoryRepository.findById(tx.getCategoryId()).map(Category::getName)
                                .orElse("Desconhecida"),
                        Collectors.reducing(BigDecimal.ZERO,
                                tx -> exchangeRateService.convert(tx.getEffectiveAmount(),
                                        walletCurrencies.getOrDefault(tx.getWalletId(), Currency.BRL),
                                        Currency.BRL),
                                BigDecimal::add)));
    }

    private BigDecimal calculateDifference(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? new BigDecimal("100") : BigDecimal.ZERO;
        }
        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }
}
