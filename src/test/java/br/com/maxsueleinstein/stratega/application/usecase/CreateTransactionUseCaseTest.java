package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CreateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.usecase.impl.CreateTransactionUseCaseImpl;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.TransactionType;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateTransactionUseCaseTest {

    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository budgetRepository;
    private br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository;
    private org.springframework.context.ApplicationEventPublisher eventPublisher;
    private CreateTransactionUseCase createTransactionUseCase;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        walletRepository = Mockito.mock(WalletRepository.class);
        budgetRepository = Mockito.mock(br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository.class);
        categoryRepository = Mockito.mock(br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository.class);
        eventPublisher = Mockito.mock(org.springframework.context.ApplicationEventPublisher.class);
        createTransactionUseCase = new CreateTransactionUseCaseImpl(transactionRepository, walletRepository, budgetRepository, categoryRepository, eventPublisher);
    }

    @Test
    void shouldCreateIncomeTransactionAndUpdateWalletBalance() {
        UUID walletId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, "Main", BigDecimal.valueOf(100), UUID.randomUUID());

        CreateTransactionRequest request = new CreateTransactionRequest(
                "Salário", BigDecimal.valueOf(500), LocalDateTime.now(), TransactionType.INCOME, walletId, categoryId,
                1, 1);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            return new Transaction(UUID.randomUUID(), t.getDescription(), t.getAmount(), t.getDate(), t.getType(),
                    t.getWalletId(), t.getCategoryId(), null);
        });

        java.util.List<TransactionResponse> responses = createTransactionUseCase.execute(request);
        TransactionResponse response = responses.get(0);

        assertNotNull(response.id());
        assertEquals("Salário", response.description());
        assertEquals(BigDecimal.valueOf(500), response.amount());

        // Verifica se o saldo foi atualizado (100 + 500 = 600)
        assertEquals(BigDecimal.valueOf(600), wallet.getBalance());
        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(Transaction.class));
    }
}
