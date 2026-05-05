package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.BudgetResponse;
import br.com.maxsueleinstein.stratega.domain.model.Budget;
import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.TransactionType;
import br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository;
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

class GetBudgetsUseCaseImplTest {

    private BudgetRepository budgetRepository;
    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    private GetBudgetsUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        budgetRepository = mock(BudgetRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        useCase = new GetBudgetsUseCaseImpl(budgetRepository, transactionRepository, categoryRepository);
    }

    @Test
    @DisplayName("Deve calcular corretamente os gastos atuais e a porcentagem da meta")
    void shouldCalculateCurrentSpentAndPercentage() {
        UUID userId = UUID.randomUUID();
        UUID catId = UUID.randomUUID();

        when(categoryRepository.findById(catId)).thenReturn(Optional.of(new Category(catId, "Supermercado", br.com.maxsueleinstein.stratega.domain.model.CategoryType.EXPENSE, userId)));

        Budget budget = new Budget(UUID.randomUUID(), userId, catId, new BigDecimal("1000.00"), 5, 2026);
        when(budgetRepository.findByUserIdAndMonthAndYear(userId, 5, 2026)).thenReturn(List.of(budget));

        Transaction expense1 = new Transaction(UUID.randomUUID(), "Compra 1", new BigDecimal("250.00"), LocalDateTime.of(2026, 5, 10, 0, 0), TransactionType.EXPENSE, UUID.randomUUID(), catId, null);
        Transaction expense2 = new Transaction(UUID.randomUUID(), "Compra 2", new BigDecimal("500.00"), LocalDateTime.of(2026, 5, 20, 0, 0), TransactionType.EXPENSE, UUID.randomUUID(), catId, null);
        
        when(transactionRepository.findByUserId(userId)).thenReturn(List.of(expense1, expense2));

        List<BudgetResponse> responses = useCase.execute(userId, 5, 2026);

        assertEquals(1, responses.size());
        BudgetResponse res = responses.get(0);

        assertEquals("Supermercado", res.categoryName());
        assertEquals(new BigDecimal("1000.00"), res.amountLimit());
        assertEquals(new BigDecimal("750.00"), res.currentSpent());
        assertEquals(new BigDecimal("75.00"), res.percentageUsed()); // 750 is 75% of 1000
        assertEquals(false, res.isOverBudget());
    }

    @Test
    @DisplayName("Deve considerar netAmount (despesa líquida) ao calcular gastos do orçamento e marcar isOverBudget")
    void shouldCalculateUsingNetAmountAndMarkOverBudget() {
        UUID userId = UUID.randomUUID();
        UUID catId = UUID.randomUUID();

        when(categoryRepository.findById(catId)).thenReturn(Optional.of(new Category(catId, "Restaurante", br.com.maxsueleinstein.stratega.domain.model.CategoryType.EXPENSE, userId)));

        Budget budget = new Budget(UUID.randomUUID(), userId, catId, new BigDecimal("100.00"), 5, 2026);
        when(budgetRepository.findByUserIdAndMonthAndYear(userId, 5, 2026)).thenReturn(List.of(budget));

        // Transação de 500 reais, mas cota do usuário (netAmount) é 110 (rachado com amigos)
        Transaction expense = new Transaction(UUID.randomUUID(), "Jantar", new BigDecimal("500.00"), new BigDecimal("110.00"), 
                LocalDateTime.of(2026, 5, 10, 0, 0), TransactionType.EXPENSE, UUID.randomUUID(), catId, null);
        
        when(transactionRepository.findByUserId(userId)).thenReturn(List.of(expense));

        List<BudgetResponse> responses = useCase.execute(userId, 5, 2026);

        assertEquals(1, responses.size());
        BudgetResponse res = responses.get(0);

        assertEquals(new BigDecimal("110.00"), res.currentSpent());
        assertEquals(new BigDecimal("110.00"), res.percentageUsed());
        assertEquals(true, res.isOverBudget());
    }
}
