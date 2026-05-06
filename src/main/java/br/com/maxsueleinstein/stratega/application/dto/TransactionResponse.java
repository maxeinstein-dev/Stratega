package br.com.maxsueleinstein.stratega.application.dto;

import br.com.maxsueleinstein.stratega.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        String description,
        BigDecimal amount,
        BigDecimal netAmount,
        LocalDateTime date,
        TransactionType type,
        UUID walletId,
        UUID categoryId,
        UUID linkedTransactionId,
        UUID groupId
) {}
