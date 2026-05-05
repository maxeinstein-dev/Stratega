package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CategoryResponse;
import br.com.maxsueleinstein.stratega.application.dto.UpdateCategoryRequest;

import java.util.UUID;

public interface UpdateCategoryUseCase {
    CategoryResponse execute(UUID categoryId, UUID userId, UpdateCategoryRequest request);
}
