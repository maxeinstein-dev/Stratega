package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.SpendingTrendResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetSpendingTrendUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Currency;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GetSpendingTrendUseCaseImpl implements GetSpendingTrendUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final ExchangeRateService exchangeRateService;

    public GetSpendingTrendUseCaseImpl(TransactionRepository transactionRepository,
                                     WalletRepository walletRepository,
                                     ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public SpendingTrendResponse execute(UUID userId, int month, int year) {
        Map<UUID, Currency> walletCurrencies = walletRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(Wallet::getId, Wallet::getCurrency));

        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(tx -> tx.isExpense() &&
                        tx.getDate().getMonthValue() == month &&
                        tx.getDate().getYear() == year)
                .toList();

        Map<LocalDate, BigDecimal> dailyTotals = new TreeMap<>();
        
        // Initialize all days of the month with zero
        LocalDate firstDay = LocalDate.of(year, month, 1);
        int daysInMonth = firstDay.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            dailyTotals.put(LocalDate.of(year, month, i), BigDecimal.ZERO);
        }

        for (Transaction tx : transactions) {
            LocalDate date = tx.getDate().toLocalDate();
            Currency txCurrency = walletCurrencies.getOrDefault(tx.getWalletId(), Currency.BRL);
            BigDecimal amountBrl = exchangeRateService.convert(tx.getEffectiveAmount(), txCurrency, Currency.BRL);
            
            dailyTotals.merge(date, amountBrl, BigDecimal::add);
        }

        List<SpendingTrendResponse.DataPoint> trend = dailyTotals.entrySet().stream()
                .map(e -> new SpendingTrendResponse.DataPoint(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new SpendingTrendResponse(trend);
    }
}
