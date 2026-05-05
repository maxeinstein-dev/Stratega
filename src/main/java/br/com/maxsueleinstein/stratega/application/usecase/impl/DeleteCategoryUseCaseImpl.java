package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.DeleteCategoryUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;

import java.util.UUID;

public class DeleteCategoryUseCaseImpl implements DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public DeleteCategoryUseCaseImpl(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void execute(UUID categoryId, UUID userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (category.getUserId() == null) {
            throw new ForbiddenException("Categorias padrão do sistema não podem ser excluídas");
        }

        if (!category.getUserId().equals(userId)) {
            throw new ForbiddenException("Você não tem permissão para excluir esta categoria");
        }

        if (transactionRepository.existsByCategoryId(categoryId)) {
            throw new ForbiddenException("Não é possível excluir uma categoria que possui transações vinculadas");
        }

        categoryRepository.deleteById(categoryId);
    }
}
