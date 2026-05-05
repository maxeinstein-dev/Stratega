package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.BudgetRequest;
import br.com.maxsueleinstein.stratega.application.dto.BudgetResponse;
import br.com.maxsueleinstein.stratega.application.usecase.SetBudgetUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Budget;
import br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SetBudgetUseCaseImpl implements SetBudgetUseCase {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    public SetBudgetUseCaseImpl(BudgetRepository budgetRepository, CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public BudgetResponse execute(UUID userId, BudgetRequest request) {
        if (categoryRepository.findById(request.categoryId()).isEmpty()) {
            throw new ResourceNotFoundException("Categoria não encontrada.");
        }

        Optional<Budget> existingBudget = budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
                userId, request.categoryId(), request.month(), request.year());

        Budget budgetToSave;
        if (existingBudget.isPresent()) {
            budgetToSave = existingBudget.get();
            budgetToSave.updateAmountLimit(request.amountLimit());
        } else {
            budgetToSave = new Budget(
                    UUID.randomUUID(),
                    userId,
                    request.categoryId(),
                    request.amountLimit(),
                    request.month(),
                    request.year()
            );
        }

        Budget savedBudget = budgetRepository.save(budgetToSave);

        return new BudgetResponse(
                savedBudget.getId(),
                savedBudget.getCategoryId(),
                null, 
                savedBudget.getAmountLimit(),
                BigDecimal.ZERO, 
                BigDecimal.ZERO, 
                false, // isOverBudget
                savedBudget.getMonth(),
                savedBudget.getYear()
        );
    }
}
