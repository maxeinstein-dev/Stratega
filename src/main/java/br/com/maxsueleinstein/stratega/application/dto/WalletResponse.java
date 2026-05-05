package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.maxsueleinstein.stratega.domain.model.Currency;

public record WalletResponse(
        UUID id,
        String name,
        BigDecimal balance,
        UUID userId,
        Currency currency,
        boolean active
) {}
