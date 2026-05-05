package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.CategoryResponse;
import br.com.maxsueleinstein.stratega.application.dto.UpdateCategoryRequest;
import br.com.maxsueleinstein.stratega.application.usecase.UpdateCategoryUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;

import java.util.UUID;

public class UpdateCategoryUseCaseImpl implements UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public UpdateCategoryUseCaseImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse execute(UUID categoryId, UUID userId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (category.getUserId() == null) {
            throw new ForbiddenException("Categorias padrão do sistema não podem ser editadas");
        }

        if (!category.getUserId().equals(userId)) {
            throw new ForbiddenException("Você não tem permissão para editar esta categoria");
        }

        category.updateName(request.name());
        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getType(),
                savedCategory.getUserId()
        );
    }
}
