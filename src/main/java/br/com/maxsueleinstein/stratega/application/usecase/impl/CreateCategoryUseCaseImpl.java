package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.CategoryResponse;
import br.com.maxsueleinstein.stratega.application.dto.CreateCategoryRequest;
import br.com.maxsueleinstein.stratega.application.usecase.CreateCategoryUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;

public class CreateCategoryUseCaseImpl implements CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public CreateCategoryUseCaseImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse execute(CreateCategoryRequest request) {
        Category category = new Category(null, request.name(), request.type(), request.userId());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getType(),
                savedCategory.getUserId()
        );
    }
}
