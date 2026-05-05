package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteWalletUseCaseImplTest {

    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private DeleteWalletUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        useCase = new DeleteWalletUseCaseImpl(walletRepository, transactionRepository);
    }

    @Test
    @DisplayName("Deve excluir fisicamente se não houver transações")
    void shouldHardDeleteWhenNoTransactions() {
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, "Wallet 1", BigDecimal.ZERO, userId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.existsByWalletId(walletId)).thenReturn(false);

        useCase.execute(walletId, userId);

        verify(walletRepository).deleteById(walletId);
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve arquivar (soft delete) se houver transações")
    void shouldSoftDeleteWhenHasTransactions() {
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, "Wallet 1", BigDecimal.ZERO, userId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.existsByWalletId(walletId)).thenReturn(true);

        useCase.execute(walletId, userId);

        assertFalse(wallet.isActive());
        verify(walletRepository).save(wallet);
        verify(walletRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se não for o dono")
    void shouldThrowExceptionWhenNotOwner() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, "Wallet 1", BigDecimal.ZERO, otherUserId);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(ForbiddenException.class, () -> useCase.execute(walletId, userId));
    }

    @Test
    @DisplayName("Deve lançar exceção se não encontrar")
    void shouldThrowExceptionWhenNotFound() {
        UUID walletId = UUID.randomUUID();
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(walletId, UUID.randomUUID()));
    }
}
