package br.com.maxsueleinstein.stratega.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(
    @NotBlank(message = "O nome da categoria é obrigatório")
    String name
) {}
