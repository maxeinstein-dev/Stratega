package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.maxsueleinstein.stratega.domain.model.Currency;

public record CreateWalletRequest(
        String name,
        BigDecimal initialBalance,
        UUID userId,
        Currency currency
) {}
