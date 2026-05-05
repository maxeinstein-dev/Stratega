package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface FindCategoriesByUserIdUseCase {
    List<CategoryResponse> execute(UUID userId);
}
