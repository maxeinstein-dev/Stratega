package br.com.maxsueleinstein.stratega.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    @DisplayName("Deve criar uma carteira válida com saldo zero por padrão")
    void shouldCreateValidWalletWithZeroBalance() {
        UUID userId = UUID.randomUUID();
        Wallet wallet = new Wallet(null, "Carteira Geral", null, userId);

        assertNotNull(wallet.getId());
        assertEquals("Carteira Geral", wallet.getName());
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
        assertEquals(userId, wallet.getUserId());
    }

    @Test
    @DisplayName("Deve criar uma carteira válida com saldo inicial fornecido")
    void shouldCreateValidWalletWithInitialBalance() {
        UUID userId = UUID.randomUUID();
        BigDecimal initialBalance = new BigDecimal("1000.50");
        Wallet wallet = new Wallet(null, "Investimentos", initialBalance, userId);

        assertEquals(initialBalance, wallet.getBalance());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome for nulo ou vazio")
    void shouldThrowExceptionWhenNameIsInvalid() {
        UUID userId = UUID.randomUUID();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            new Wallet(null, "", null, userId)
        );
        assertEquals("O nome da carteira não pode estar em branco", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o userId for nulo")
    void shouldThrowExceptionWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            new Wallet(null, "Carteira Geral", null, null)
        );
        assertEquals("O usuário dono da carteira é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve adicionar fundos corretamente")
    void shouldAddFundsSuccessfully() {
        Wallet wallet = new Wallet(null, "Geral", new BigDecimal("100.00"), UUID.randomUUID());
        wallet.addFunds(new BigDecimal("50.50"));

        assertEquals(new BigDecimal("150.50"), wallet.getBalance());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar fundos negativos ou nulos")
    void shouldThrowExceptionWhenAddingInvalidFunds() {
        Wallet wallet = new Wallet(null, "Geral", BigDecimal.ZERO, UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> wallet.addFunds(null));
        assertThrows(IllegalArgumentException.class, () -> wallet.addFunds(new BigDecimal("-10.00")));
        assertThrows(IllegalArgumentException.class, () -> wallet.addFunds(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Deve remover fundos corretamente permitindo saldo negativo")
    void shouldRemoveFundsSuccessfully() {
        Wallet wallet = new Wallet(null, "Geral", new BigDecimal("100.00"), UUID.randomUUID());
        wallet.removeFunds(new BigDecimal("150.00"));

        assertEquals(new BigDecimal("-50.00"), wallet.getBalance());
    }

    @Test
    @DisplayName("Deve lançar exceção ao remover fundos negativos ou nulos")
    void shouldThrowExceptionWhenRemovingInvalidFunds() {
        Wallet wallet = new Wallet(null, "Geral", BigDecimal.ZERO, UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> wallet.removeFunds(null));
        assertThrows(IllegalArgumentException.class, () -> wallet.removeFunds(new BigDecimal("-10.00")));
        assertThrows(IllegalArgumentException.class, () -> wallet.removeFunds(BigDecimal.ZERO));
    }
}
