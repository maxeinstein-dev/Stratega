package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        UUID id,
        String name,
        BigDecimal balance,
        UUID userId
) {}
