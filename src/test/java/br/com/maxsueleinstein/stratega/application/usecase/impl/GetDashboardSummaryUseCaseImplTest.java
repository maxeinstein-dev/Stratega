package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.DashboardSummaryResponse;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.TransactionType;
import br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetDashboardSummaryUseCaseImplTest {

    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    private GetDashboardSummaryUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        useCase = new GetDashboardSummaryUseCaseImpl(transactionRepository, categoryRepository);
    }

    @Test
    @DisplayName("Deve retornar resumo correto filtrado por mes e ano")
    void shouldReturnCorrectSummary() {
        UUID userId = UUID.randomUUID();
        UUID catFoodId = UUID.randomUUID();
        UUID catFunId = UUID.randomUUID();

        when(categoryRepository.findById(catFoodId)).thenReturn(Optional.of(new Category(catFoodId, "Alimentação", br.com.maxsueleinstein.stratega.domain.model.CategoryType.EXPENSE, userId)));
        when(categoryRepository.findById(catFunId)).thenReturn(Optional.of(new Category(catFunId, "Lazer", br.com.maxsueleinstein.stratega.domain.model.CategoryType.EXPENSE, userId)));

        Transaction income = new Transaction(UUID.randomUUID(), "Salario", new BigDecimal("5000.00"), LocalDateTime.of(2026, 5, 10, 0, 0), TransactionType.INCOME, UUID.randomUUID(), null, null);
        Transaction expense1 = new Transaction(UUID.randomUUID(), "Mercado", new BigDecimal("800.00"), LocalDateTime.of(2026, 5, 15, 0, 0), TransactionType.EXPENSE, UUID.randomUUID(), catFoodId, null);
        Transaction expense2 = new Transaction(UUID.randomUUID(), "Cinema", new BigDecimal("200.00"), LocalDateTime.of(2026, 5, 20, 0, 0), TransactionType.EXPENSE, UUID.randomUUID(), catFunId, null);
        Transaction outOfMonth = new Transaction(UUID.randomUUID(), "Fora do mes", new BigDecimal("100.00"), LocalDateTime.of(2026, 6, 1, 0, 0), TransactionType.EXPENSE, UUID.randomUUID(), catFunId, null);

        when(transactionRepository.findByUserId(userId)).thenReturn(List.of(income, expense1, expense2, outOfMonth));

        DashboardSummaryResponse response = useCase.execute(userId, 5, 2026);

        assertEquals(new BigDecimal("5000.00"), response.totalIncome());
        assertEquals(new BigDecimal("1000.00"), response.totalExpense());
        assertEquals(new BigDecimal("4000.00"), response.balance());
        
        assertEquals(new BigDecimal("800.00"), response.expensesByCategory().get("Alimentação"));
        assertEquals(new BigDecimal("200.00"), response.expensesByCategory().get("Lazer"));
    }
}
