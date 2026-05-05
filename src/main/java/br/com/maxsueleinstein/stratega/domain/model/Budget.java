package br.com.maxsueleinstein.stratega.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Budget {
    private final UUID id;
    private final UUID userId;
    private final UUID categoryId;
    private BigDecimal amountLimit;
    private final int month;
    private final int year;

    public Budget(UUID id, UUID userId, UUID categoryId, BigDecimal amountLimit, int month, int year) {
        if (amountLimit == null || amountLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O limite do orçamento deve ser maior ou igual a zero.");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Mês inválido.");
        }

        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amountLimit = amountLimit;
        this.month = month;
        this.year = year;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public BigDecimal getAmountLimit() {
        return amountLimit;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void updateAmountLimit(BigDecimal newLimit) {
        if (newLimit == null || newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O limite do orçamento deve ser maior ou igual a zero.");
        }
        this.amountLimit = newLimit;
    }
}
