package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Summary of financial status for a period.
 * All numeric values are consolidated in the user's base currency (default: BRL).
 */
public record DashboardSummaryResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        Map<String, BigDecimal> expensesByCategory
) {}
