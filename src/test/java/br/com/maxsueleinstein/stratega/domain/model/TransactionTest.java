package br.com.maxsueleinstein.stratega.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    @DisplayName("Deve criar uma transação válida")
    void shouldCreateValidTransaction() {
        UUID walletId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal amount = new BigDecimal("150.50");

        Transaction transaction = new Transaction(
                null, "Conta de energia", amount, now,
                TransactionType.EXPENSE, walletId, categoryId, null
        );

        assertNotNull(transaction.getId());
        assertEquals("Conta de energia", transaction.getDescription());
        assertEquals(amount, transaction.getAmount());
        assertEquals(now, transaction.getDate());
        assertEquals(TransactionType.EXPENSE, transaction.getType());
        assertEquals(walletId, transaction.getWalletId());
        assertEquals(categoryId, transaction.getCategoryId());
        assertNull(transaction.getLinkedTransactionId());
        assertTrue(transaction.isExpense());
        assertFalse(transaction.isIncome());
    }

    @Test
    @DisplayName("Deve identificar corretamente receitas e despesas")
    void shouldCorrectlyIdentifyIncomeAndExpense() {
        UUID walletId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Transaction income = new Transaction(null, "Salário", BigDecimal.TEN, now, TransactionType.INCOME, walletId, null, null);
        Transaction transferIn = new Transaction(null, "Recebimento", BigDecimal.TEN, now, TransactionType.TRANSFER_IN, walletId, null, null);
        
        Transaction expense = new Transaction(null, "Mercado", BigDecimal.TEN, now, TransactionType.EXPENSE, walletId, null, null);
        Transaction transferOut = new Transaction(null, "Envio", BigDecimal.TEN, now, TransactionType.TRANSFER_OUT, walletId, null, null);

        assertTrue(income.isIncome());
        assertTrue(transferIn.isIncome());
        assertFalse(income.isExpense());

        assertTrue(expense.isExpense());
        assertTrue(transferOut.isExpense());
        assertFalse(expense.isIncome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao faltar campos obrigatórios")
    void shouldThrowExceptionWhenMissingRequiredFields() {
        UUID walletId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // Sem descrição
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(null, "", BigDecimal.TEN, now, TransactionType.EXPENSE, walletId, null, null)
        );

        // Valor inválido
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(null, "Teste", BigDecimal.ZERO, now, TransactionType.EXPENSE, walletId, null, null)
        );

        // Sem data
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(null, "Teste", BigDecimal.TEN, null, TransactionType.EXPENSE, walletId, null, null)
        );

        // Sem tipo
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(null, "Teste", BigDecimal.TEN, now, null, walletId, null, null)
        );

        // Sem carteira
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(null, "Teste", BigDecimal.TEN, now, TransactionType.EXPENSE, null, null, null)
        );
    }
}
