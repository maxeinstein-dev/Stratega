package br.com.maxsueleinstein.stratega.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetExceededEvent(
    UUID userId,
    String categoryName,
    BigDecimal limit,
    BigDecimal currentSpent
) {}
