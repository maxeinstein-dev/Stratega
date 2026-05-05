package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetRequest(
        UUID categoryId,
        BigDecimal amountLimit,
        int month,
        int year
) {}
