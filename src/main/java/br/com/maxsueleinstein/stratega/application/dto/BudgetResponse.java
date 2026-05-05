package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetResponse(
        UUID id,
        UUID categoryId,
        String categoryName,
        BigDecimal amountLimit,
        BigDecimal currentSpent,
        BigDecimal percentageUsed,
        boolean isOverBudget,
        int month,
        int year
) {}
