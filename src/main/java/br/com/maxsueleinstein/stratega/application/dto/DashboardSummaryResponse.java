package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.Map;

public record DashboardSummaryResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        Map<String, BigDecimal> expensesByCategory
) {}
