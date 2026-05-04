package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record AddGroupExpenseRequest(
    UUID groupId,
    String description,
    BigDecimal amount,
    UUID paidByMemberId,
    String splitType, // "UNIFORM", "EXACT", "PERCENTAGE", "SHARE"
    Map<UUID, Object> splitValues // Valores dependendo do tipo (BigDecimal para EXACT, Double para PERCENTAGE, Integer para SHARE)
) {}
