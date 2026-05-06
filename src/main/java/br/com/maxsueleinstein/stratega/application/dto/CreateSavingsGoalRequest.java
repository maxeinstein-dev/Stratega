package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateSavingsGoalRequest(
        String name,
        BigDecimal targetAmount,
        LocalDateTime deadline
) {
}
