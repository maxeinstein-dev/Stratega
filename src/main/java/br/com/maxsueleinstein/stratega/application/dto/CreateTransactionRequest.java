package br.com.maxsueleinstein.stratega.application.dto;

import br.com.maxsueleinstein.stratega.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTransactionRequest(
        String description,
        BigDecimal amount,
        LocalDateTime date,
        TransactionType type,
        UUID walletId,
        UUID categoryId
) {}
