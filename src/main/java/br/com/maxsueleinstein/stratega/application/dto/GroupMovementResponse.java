package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GroupMovementResponse(
    UUID id,
    String description,
    BigDecimal amount,
    String type, // "EXPENSE" ou "SETTLEMENT"
    LocalDateTime date,
    String paidByName,
    UUID paidByMemberId
) {}
