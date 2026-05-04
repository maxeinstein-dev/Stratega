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
    private CreateTransactionUseCase createTransactionUseCase;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        walletRepository = Mockito.mock(WalletRepository.class);
        createTransactionUseCase = new CreateTransactionUseCaseImpl(transactionRepository, walletRepository);
    }

    @Test
    void shouldCreateIncomeTransactionAndUpdateWalletBalance() {
        UUID walletId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, "Main", BigDecimal.valueOf(100), UUID.randomUUID());
        
        CreateTransactionRequest request = new CreateTransactionRequest(
                "Salário", BigDecimal.valueOf(500), LocalDateTime.now(), TransactionType.INCOME, walletId, categoryId
        );

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            return new Transaction(UUID.randomUUID(), t.getDescription(), t.getAmount(), t.getDate(), t.getType(), t.getWalletId(), t.getCategoryId(), null);
        });

        TransactionResponse response = createTransactionUseCase.execute(request);

        assertNotNull(response.id());
        assertEquals("Salário", response.description());
        assertEquals(BigDecimal.valueOf(500), response.amount());
        
        // Verifica se o saldo foi atualizado (100 + 500 = 600)
        assertEquals(BigDecimal.valueOf(600), wallet.getBalance());
        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(Transaction.class));
    }
}
