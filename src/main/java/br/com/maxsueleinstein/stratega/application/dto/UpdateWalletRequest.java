package br.com.maxsueleinstein.stratega.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateWalletRequest(
    @NotBlank(message = "O nome da carteira é obrigatório")
    String name
) {}
