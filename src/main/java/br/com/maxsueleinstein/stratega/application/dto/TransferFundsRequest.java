package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferFundsRequest(
        String description,
        BigDecimal amount,
        LocalDateTime date,
        UUID originWalletId,
        UUID destinationWalletId,
        UUID categoryId
) {}
