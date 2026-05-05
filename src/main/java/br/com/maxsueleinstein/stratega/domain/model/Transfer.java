package br.com.maxsueleinstein.stratega.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transfer {
    private final UUID id;
    private final UUID userId;
    private final UUID fromWalletId;
    private final UUID toWalletId;
    private final BigDecimal amount;
    private final String description;
    private final LocalDateTime date;
    private final UUID transactionOutId;
    private final UUID transactionInId;

    public Transfer(UUID id, UUID userId, UUID fromWalletId, UUID toWalletId, BigDecimal amount, String description, LocalDateTime date, UUID transactionOutId, UUID transactionInId) {
        this.id = id != null ? id : UUID.randomUUID();
        this.userId = userId;
        this.fromWalletId = fromWalletId;
        this.toWalletId = toWalletId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.transactionOutId = transactionOutId;
        this.transactionInId = transactionInId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getFromWalletId() {
        return fromWalletId;
    }

    public UUID getToWalletId() {
        return toWalletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public UUID getTransactionOutId() {
        return transactionOutId;
    }

    public UUID getTransactionInId() {
        return transactionInId;
    }
}
