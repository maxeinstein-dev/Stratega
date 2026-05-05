package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CategoryResponse;
import br.com.maxsueleinstein.stratega.application.dto.CreateCategoryRequest;

public interface CreateCategoryUseCase {
    CategoryResponse execute(CreateCategoryRequest request);
}
