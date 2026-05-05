package br.com.maxsueleinstein.stratega.application.usecase.impl;

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
import static org.mockito.Mockito.*;

class DeleteTransactionUseCaseImplTest {

    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private br.com.maxsueleinstein.stratega.application.usecase.DeleteTransferUseCase deleteTransferUseCase;
    private DeleteTransactionUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        walletRepository = mock(WalletRepository.class);
        deleteTransferUseCase = mock(br.com.maxsueleinstein.stratega.application.usecase.DeleteTransferUseCase.class);
        useCase = new DeleteTransactionUseCaseImpl(transactionRepository, walletRepository, deleteTransferUseCase);
    }

    @Test
    @DisplayName("Deve deletar despesa e reverter saldo (aumentar)")
    void shouldDeleteExpenseAndRevertBalance() {
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        UUID txId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, "Wallet", new BigDecimal("100.00"), userId);
        Transaction tx = new Transaction(txId, "Lunch", new BigDecimal("30.00"), null, LocalDateTime.now(),
                TransactionType.EXPENSE, walletId, UUID.randomUUID(), null);

        when(transactionRepository.findById(txId)).thenReturn(Optional.of(tx));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        useCase.execute(txId, userId);

        assertEquals(new BigDecimal("130.00"), wallet.getBalance());
        verify(walletRepository).save(wallet);
        verify(transactionRepository).deleteById(txId);
    }

    @Test
    @DisplayName("Deve deletar receita e reverter saldo (diminuir)")
    void shouldDeleteIncomeAndRevertBalance() {
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        UUID txId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, "Wallet", new BigDecimal("100.00"), userId);
        Transaction tx = new Transaction(txId, "Salary", new BigDecimal("30.00"), null, LocalDateTime.now(),
                TransactionType.INCOME, walletId, UUID.randomUUID(), null);

        when(transactionRepository.findById(txId)).thenReturn(Optional.of(tx));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        useCase.execute(txId, userId);

        assertEquals(new BigDecimal("70.00"), wallet.getBalance());
        verify(walletRepository).save(wallet);
        verify(transactionRepository).deleteById(txId);
    }

    @Test
    @DisplayName("Deve delegar para DeleteTransferUseCase se for transferência")
    void shouldDelegateToDeleteTransferUseCase() {
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        UUID txId = UUID.randomUUID();
        Transaction tx = new Transaction(txId, "Transfer", new BigDecimal("30.00"), null, LocalDateTime.now(),
                TransactionType.TRANSFER_OUT, walletId, null, UUID.randomUUID());

        when(transactionRepository.findById(txId)).thenReturn(Optional.of(tx));

        useCase.execute(txId, userId);

        verify(deleteTransferUseCase).execute(txId, userId);
        verify(transactionRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se não for dono da carteira")
    void shouldThrowExceptionWhenNotOwner() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        UUID txId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, "Wallet", new BigDecimal("100.00"), otherUserId);
        Transaction tx = new Transaction(txId, "Lunch", new BigDecimal("30.00"), null, LocalDateTime.now(),
                TransactionType.EXPENSE, walletId, UUID.randomUUID(), null);

        when(transactionRepository.findById(txId)).thenReturn(Optional.of(tx));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(ForbiddenException.class, () -> useCase.execute(txId, userId));
    }
}
