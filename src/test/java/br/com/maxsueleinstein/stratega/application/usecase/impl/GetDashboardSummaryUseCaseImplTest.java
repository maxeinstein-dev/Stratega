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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetDashboardSummaryUseCaseImplTest {

    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    private br.com.maxsueleinstein.stratega.domain.repository.WalletRepository walletRepository;
    private br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService exchangeRateService;
    private GetDashboardSummaryUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        walletRepository = mock(br.com.maxsueleinstein.stratega.domain.repository.WalletRepository.class);
        exchangeRateService = mock(br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService.class);
        
        // Default mock behavior for currency conversion (identity)
        when(exchangeRateService.convert(any(BigDecimal.class), any(), any())).thenAnswer(invocation -> invocation.getArgument(0));

        useCase = new GetDashboardSummaryUseCaseImpl(transactionRepository, categoryRepository, walletRepository, exchangeRateService);
    }

    @Test
    @DisplayName("Deve retornar resumo correto filtrado por mes e ano")
    void shouldReturnCorrectSummary() {
        UUID userId = UUID.randomUUID();
        UUID catFoodId = UUID.randomUUID();
        UUID catFunId = UUID.randomUUID();

        when(categoryRepository.findById(catFoodId)).thenReturn(Optional.of(new Category(catFoodId, "Alimentação", br.com.maxsueleinstein.stratega.domain.model.CategoryType.EXPENSE, userId)));
        when(categoryRepository.findById(catFunId)).thenReturn(Optional.of(new Category(catFunId, "Lazer", br.com.maxsueleinstein.stratega.domain.model.CategoryType.EXPENSE, userId)));

        UUID walletId = UUID.randomUUID();
        br.com.maxsueleinstein.stratega.domain.model.Wallet wallet = new br.com.maxsueleinstein.stratega.domain.model.Wallet(walletId, "Wallet", BigDecimal.ZERO, userId);
        when(walletRepository.findByUserId(userId)).thenReturn(List.of(wallet));

        Transaction income = new Transaction(UUID.randomUUID(), "Salario", new BigDecimal("5000.00"), LocalDateTime.of(2026, 5, 10, 0, 0), TransactionType.INCOME, walletId, null, null);
        Transaction expense1 = new Transaction(UUID.randomUUID(), "Mercado", new BigDecimal("800.00"), LocalDateTime.of(2026, 5, 15, 0, 0), TransactionType.EXPENSE, walletId, catFoodId, null);
        Transaction expense2 = new Transaction(UUID.randomUUID(), "Cinema", new BigDecimal("200.00"), LocalDateTime.of(2026, 5, 20, 0, 0), TransactionType.EXPENSE, walletId, catFunId, null);
        Transaction outOfMonth = new Transaction(UUID.randomUUID(), "Fora do mes", new BigDecimal("100.00"), LocalDateTime.of(2026, 6, 1, 0, 0), TransactionType.EXPENSE, walletId, catFunId, null);

        when(transactionRepository.findByUserId(userId)).thenReturn(List.of(income, expense1, expense2, outOfMonth));

        DashboardSummaryResponse response = useCase.execute(userId, 5, 2026);

        assertEquals(new BigDecimal("5000.00"), response.totalIncome());
        assertEquals(new BigDecimal("1000.00"), response.totalExpense());
        assertEquals(new BigDecimal("4000.00"), response.balance());
        
        assertEquals(new BigDecimal("800.00"), response.expensesByCategory().get("Alimentação"));
        assertEquals(new BigDecimal("200.00"), response.expensesByCategory().get("Lazer"));
    }
}
