package br.com.maxsueleinstein.stratega.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ReceitaRequestDTO(@NotBlank String nome,
                                Long categoriaId,
                                String categoriaNome,
                                @PositiveOrZero Double valorPrevisto) {
    public static class DespesaRequestDTO {
    }
}