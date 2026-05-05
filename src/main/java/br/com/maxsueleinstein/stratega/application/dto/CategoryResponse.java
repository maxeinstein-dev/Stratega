package br.com.maxsueleinstein.stratega.application.dto;

import br.com.maxsueleinstein.stratega.domain.model.CategoryType;
import java.util.UUID;

public record CategoryResponse(
    UUID id,
    String name,
    CategoryType type,
    UUID userId
) {}
