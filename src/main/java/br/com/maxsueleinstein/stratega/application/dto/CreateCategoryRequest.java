package br.com.maxsueleinstein.stratega.application.dto;

import br.com.maxsueleinstein.stratega.domain.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateCategoryRequest(
    @NotBlank(message = "O nome da categoria é obrigatório")
    String name,
    @NotNull(message = "O tipo da categoria é obrigatório")
    CategoryType type,
    UUID userId // Pode ser null para categorias globais (se o sistema permitir via admin, mas aqui o usecase valida)
) {}
