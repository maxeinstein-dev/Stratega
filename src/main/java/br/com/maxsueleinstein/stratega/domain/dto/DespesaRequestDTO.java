package br.com.maxsueleinstein.stratega.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record DespesaRequestDTO(@NotBlank String nome,
                               Long categoriaId,
                               String categoriaNome,
                               @PositiveOrZero Double valorPrevisto) {
}