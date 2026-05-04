package br.com.maxsueleinstein.stratega.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuggestedTransferTest {

    @Test
    @DisplayName("Deve sugerir transferências para simplificar dívidas")
    void shouldSuggestTransfers() {
        ExpenseGroupMember alice = new ExpenseGroupMember(UUID.randomUUID(), "Alice", null);
        ExpenseGroupMember bob = new ExpenseGroupMember(UUID.randomUUID(), "Bob", null);
        ExpenseGroupMember charlie = new ExpenseGroupMember(UUID.randomUUID(), "Charlie", null);

        List<ExpenseGroupMember> members = List.of(alice, bob, charlie);

        // Alice: +50, Bob: -20, Charlie: -30
        Map<UUID, BigDecimal> balances = Map.of(
                alice.getId(), new BigDecimal("50.00"),
                bob.getId(), new BigDecimal("-20.00"),
                charlie.getId(), new BigDecimal("-30.00"));

        GroupBalanceCalculator calculator = new GroupBalanceCalculator();
        List<SuggestedTransfer> transfers = calculator.suggestTransfers(members, balances);

        assertEquals(2, transfers.size());

        // Esperado:
        // Bob paga 20 para Alice
        // Charlie paga 30 para Alice
        assertTrue(transfers.stream().anyMatch(t -> t.from().equals(bob) && t.to().equals(alice)
                && t.amount().compareTo(new BigDecimal("20.00")) == 0));
        assertTrue(transfers.stream().anyMatch(t -> t.from().equals(charlie) && t.to().equals(alice)
                && t.amount().compareTo(new BigDecimal("30.00")) == 0));
    }
}
