package br.com.maxsueleinstein.stratega.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record SettleDebtRequest(
    @NotNull(message = "O ID do membro que está pagando é obrigatório")
    UUID memberId,
    @NotNull(message = "O ID da carteira de destino é obrigatório")
    UUID destinationWalletId,
    @NotNull(message = "O valor da liquidação é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    BigDecimal amount,
    String description
) {}
