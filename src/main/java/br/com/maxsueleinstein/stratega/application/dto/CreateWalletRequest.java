package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateWalletRequest(
        String name,
        BigDecimal initialBalance,
        UUID userId
) {}
