package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;

public record AddFundsToGoalRequest(
        BigDecimal amount
) {
}
