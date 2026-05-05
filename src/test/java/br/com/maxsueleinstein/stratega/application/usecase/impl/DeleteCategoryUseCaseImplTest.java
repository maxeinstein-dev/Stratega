package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.model.CategoryType;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteCategoryUseCaseImplTest {

    private CategoryRepository categoryRepository;
    private TransactionRepository transactionRepository;
    private DeleteCategoryUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        useCase = new DeleteCategoryUseCaseImpl(categoryRepository, transactionRepository);
    }

    @Test
    @DisplayName("Deve excluir se for o dono e não houver transações")
    void shouldDeleteWhenOwnerAndNoTransactions() {
        UUID userId = UUID.randomUUID();
        UUID catId = UUID.randomUUID();
        Category category = new Category(catId, "Food", CategoryType.EXPENSE, userId);

        when(categoryRepository.findById(catId)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(catId)).thenReturn(false);

        useCase.execute(catId, userId);

        verify(categoryRepository).deleteById(catId);
    }

    @Test
    @DisplayName("Deve lançar exceção se for global")
    void shouldThrowExceptionWhenGlobal() {
        UUID catId = UUID.randomUUID();
        Category category = new Category(catId, "Salary", CategoryType.INCOME, null);

        when(categoryRepository.findById(catId)).thenReturn(Optional.of(category));

        assertThrows(ForbiddenException.class, () -> useCase.execute(catId, UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção se houver transações")
    void shouldThrowExceptionWhenHasTransactions() {
        UUID userId = UUID.randomUUID();
        UUID catId = UUID.randomUUID();
        Category category = new Category(catId, "Food", CategoryType.EXPENSE, userId);

        when(categoryRepository.findById(catId)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(catId)).thenReturn(true);

        assertThrows(ForbiddenException.class, () -> useCase.execute(catId, userId));
    }
}
