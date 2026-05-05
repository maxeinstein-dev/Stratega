package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.CategoryResponse;
import br.com.maxsueleinstein.stratega.application.usecase.FindCategoriesByUserIdUseCase;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FindCategoriesByUserIdUseCaseImpl implements FindCategoriesByUserIdUseCase {

    private final CategoryRepository categoryRepository;

    public FindCategoriesByUserIdUseCaseImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> execute(UUID userId) {
        return categoryRepository.findByUserIdOrGlobal(userId).stream()
                .map(cat -> new CategoryResponse(
                        cat.getId(),
                        cat.getName(),
                        cat.getType(),
                        cat.getUserId()
                ))
                .collect(Collectors.toList());
    }
}
