package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.dto.UpdateTransactionRequest;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.TransactionType;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateTransactionUseCaseImplTest {

    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private UpdateTransactionUseCaseImpl useCase;

    private UUID userId;
    private UUID walletId;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        walletRepository = mock(WalletRepository.class);
        useCase = new UpdateTransactionUseCaseImpl(transactionRepository, walletRepository);

        userId = UUID.randomUUID();
        walletId = UUID.randomUUID();
        wallet = new Wallet(walletId, "Main Wallet", new BigDecimal("1000.00"), userId);
    }

    @Test
    @DisplayName("Deve atualizar uma despesa simples e ajustar os saldos corretamente")
    void shouldUpdateSimpleExpense() {
        UUID transactionId = UUID.randomUUID();
        // O valor antigo é 100, então a carteira estaria com 100 a menos do que se não
        // houvesse despesa.
        Transaction expense = new Transaction(transactionId, "Old Desc", new BigDecimal("100.00"), LocalDateTime.now(),
                TransactionType.EXPENSE, walletId, null, null);

        UpdateTransactionRequest request = new UpdateTransactionRequest(
                "New Desc", new BigDecimal("150.00"), LocalDateTime.now(), walletId, null);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(expense));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        TransactionResponse response = useCase.execute(transactionId, userId, request);

        assertEquals("New Desc", response.description());
        assertEquals(new BigDecimal("150.00"), response.amount());

        // A carteira tinha 1000. Reverte a despesa de 100 (+100) = 1100. Aplica a nova
        // de 150 (-150) = 950.
        assertEquals(new BigDecimal("950.00"), wallet.getBalance());

        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(expense);
    }

    @Test
    @DisplayName("Deve impedir atualização se o usuário não for dono da carteira")
    void shouldForbidUpdateIfUserIsNotOwner() {
        UUID transactionId = UUID.randomUUID();
        Transaction expense = new Transaction(transactionId, "Old Desc", new BigDecimal("100.00"), LocalDateTime.now(),
                TransactionType.EXPENSE, walletId, null, null);

        UpdateTransactionRequest request = new UpdateTransactionRequest(
                "New Desc", new BigDecimal("150.00"), LocalDateTime.now(), walletId, null);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(expense));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(ForbiddenException.class, () -> {
            useCase.execute(transactionId, UUID.randomUUID(), request);
        });
    }

    @Test
    @DisplayName("Deve atualizar uma transferência (duas transações e duas carteiras)")
    void shouldUpdateTransfer() {
        UUID primaryTxId = UUID.randomUUID();
        UUID linkedTxId = UUID.randomUUID();
        UUID destWalletId = UUID.randomUUID();

        Wallet destWallet = new Wallet(destWalletId, "Dest Wallet", new BigDecimal("500.00"), userId);

        // O valor antigo da transferência é 200
        Transaction transferOut = new Transaction(primaryTxId, "Transfer OUT", new BigDecimal("200.00"),
                LocalDateTime.now(), TransactionType.TRANSFER_OUT, walletId, null, linkedTxId);
        Transaction transferIn = new Transaction(linkedTxId, "Transfer IN", new BigDecimal("200.00"),
                LocalDateTime.now(), TransactionType.TRANSFER_IN, destWalletId, null, primaryTxId);

        // Novo valor será 300
        UpdateTransactionRequest request = new UpdateTransactionRequest(
                "Transferência Atualizada", new BigDecimal("300.00"), LocalDateTime.now(), walletId, null);

        when(transactionRepository.findById(primaryTxId)).thenReturn(Optional.of(transferOut));
        when(transactionRepository.findById(linkedTxId)).thenReturn(Optional.of(transferIn));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.findById(destWalletId)).thenReturn(Optional.of(destWallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        TransactionResponse response = useCase.execute(primaryTxId, userId, request);

        assertEquals("Transferência Atualizada", response.description());
        assertEquals(new BigDecimal("300.00"), response.amount());

        // Wallet Origem: tinha 1000. Reverte 200 (+200) = 1200. Aplica 300 (-300) =
        // 900.
        assertEquals(new BigDecimal("900.00"), wallet.getBalance());

        // Wallet Destino: tinha 500. Reverte 200 (-200) = 300. Aplica 300 (+300) = 600.
        assertEquals(new BigDecimal("600.00"), destWallet.getBalance());

        verify(walletRepository).save(wallet);
        verify(walletRepository).save(destWallet);
        verify(transactionRepository).save(transferOut);
        verify(transactionRepository).save(transferIn);
    }
}
