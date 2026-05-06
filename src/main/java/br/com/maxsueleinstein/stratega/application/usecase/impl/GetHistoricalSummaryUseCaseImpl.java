package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.HistoricalSummaryResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetHistoricalSummaryUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Currency;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class GetHistoricalSummaryUseCaseImpl implements GetHistoricalSummaryUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final ExchangeRateService exchangeRateService;

    public GetHistoricalSummaryUseCaseImpl(TransactionRepository transactionRepository,
                                           WalletRepository walletRepository,
                                           ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public HistoricalSummaryResponse execute(UUID userId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        
        Map<UUID, Currency> walletCurrencies = walletRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(Wallet::getId, Wallet::getCurrency));

        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(tx -> tx.getDate() != null && !tx.getDate().isBefore(startDate))
                .collect(Collectors.toList());

        boolean groupByDay = days <= 31;
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM/yy", Locale.of("pt", "BR"));
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd/MM");

        Map<String, HistoricalSummaryResponse.PeriodSummary> summaryMap = new LinkedHashMap<>();

        // Initialize buckets
        LocalDate current = startDate.toLocalDate();
        LocalDate end = LocalDate.now();
        
        while (!current.isAfter(end)) {
            String key = groupByDay ? current.format(dayFormatter) : current.format(monthFormatter);
            summaryMap.putIfAbsent(key, new HistoricalSummaryResponse.PeriodSummary(key, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            current = groupByDay ? current.plusDays(1) : current.plusMonths(1).withDayOfMonth(1);
        }

        // Populate buckets
        for (Transaction tx : transactions) {
            String key = groupByDay ? tx.getDate().format(dayFormatter) : tx.getDate().format(monthFormatter);
            if (summaryMap.containsKey(key)) {
                Currency txCurrency = walletCurrencies.getOrDefault(tx.getWalletId(), Currency.BRL);
                BigDecimal amountBrl = exchangeRateService.convert(tx.getEffectiveAmount(), txCurrency, Currency.BRL);

                HistoricalSummaryResponse.PeriodSummary existing = summaryMap.get(key);
                BigDecimal income = existing.income();
                BigDecimal expense = existing.expense();

                if (tx.isIncome()) {
                    income = income.add(amountBrl);
                } else if (tx.isExpense()) {
                    expense = expense.add(amountBrl);
                }

                BigDecimal savings = income.subtract(expense);
                summaryMap.put(key, new HistoricalSummaryResponse.PeriodSummary(key, income, expense, savings));
            }
        }

        return new HistoricalSummaryResponse(new ArrayList<>(summaryMap.values()));
    }
}
