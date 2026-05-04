package br.com.maxsueleinstein.stratega.domain.model;

import br.com.maxsueleinstein.stratega.domain.model.split.Split;
import br.com.maxsueleinstein.stratega.domain.model.split.UniformSplit;
import br.com.maxsueleinstein.stratega.domain.model.split.UniformSplitStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupBalanceCalculatorTest {

    @Test
    @DisplayName("Deve calcular balanços simples corretamente")
    void shouldCalculateSimpleBalances() {
        ExpenseGroupMember alice = new ExpenseGroupMember(UUID.randomUUID(), "Alice", null);
        ExpenseGroupMember bob = new ExpenseGroupMember(UUID.randomUUID(), "Bob", null);
        
        // Alice pagou 100, dividido igualmente
        List<Split> splits = new ArrayList<>();
        splits.add(new UniformSplit(alice));
        splits.add(new UniformSplit(bob));
        
        GroupExpense expense = new GroupExpense(UUID.randomUUID(), "Almoço", new BigDecimal("100.00"), alice, java.time.LocalDateTime.now(), splits, new UniformSplitStrategy());
        
        List<GroupExpense> expenses = List.of(expense);
        List<ExpenseGroupMember> members = List.of(alice, bob);
        
        GroupBalanceCalculator calculator = new GroupBalanceCalculator();
        Map<UUID, BigDecimal> balances = calculator.calculate(members, expenses);
        
        // Alice: pagou 100, deve 50 -> saldo +50
        // Bob: pagou 0, deve 50 -> saldo -50
        assertEquals(new BigDecimal("50.00"), balances.get(alice.getId()));
        assertEquals(new BigDecimal("-50.00"), balances.get(bob.getId()));
    }

    @Test
    @DisplayName("Deve calcular balanços com múltiplos pagadores e despesas")
    void shouldCalculateComplexBalances() {
        ExpenseGroupMember alice = new ExpenseGroupMember(UUID.randomUUID(), "Alice", null);
        ExpenseGroupMember bob = new ExpenseGroupMember(UUID.randomUUID(), "Bob", null);
        ExpenseGroupMember charlie = new ExpenseGroupMember(UUID.randomUUID(), "Charlie", null);
        
        List<ExpenseGroupMember> members = List.of(alice, bob, charlie);
        List<GroupExpense> expenses = new ArrayList<>();
        
        // 1. Alice pagou 90, dividido por Alice, Bob, Charlie (30 cada)
        List<Split> splits1 = List.of(new UniformSplit(alice), new UniformSplit(bob), new UniformSplit(charlie));
        expenses.add(new GroupExpense(UUID.randomUUID(), "E1", new BigDecimal("90.00"), alice, java.time.LocalDateTime.now(), splits1, new UniformSplitStrategy()));
        
        // 2. Bob pagou 60, dividido por Alice, Bob (30 cada)
        List<Split> splits2 = List.of(new UniformSplit(alice), new UniformSplit(bob));
        expenses.add(new GroupExpense(UUID.randomUUID(), "E2", new BigDecimal("60.00"), bob, java.time.LocalDateTime.now(), splits2, new UniformSplitStrategy()));

        GroupBalanceCalculator calculator = new GroupBalanceCalculator();
        Map<UUID, BigDecimal> balances = calculator.calculate(members, expenses);
        
        // Alice:
        // E1: pagou 90, deve 30 -> +60
        // E2: pagou 0, deve 30 -> -30
        // Total: +30
        
        // Bob:
        // E1: pagou 0, deve 30 -> -30
        // E2: pagou 60, deve 30 -> +30
        // Total: 0
        
        // Charlie:
        // E1: pagou 0, deve 30 -> -30
        // E2: não participa -> 0
        // Total: -30

        assertEquals(new BigDecimal("30.00"), balances.get(alice.getId()));
        assertEquals(new BigDecimal("0.00"), balances.get(bob.getId()));
        assertEquals(new BigDecimal("-30.00"), balances.get(charlie.getId()));
    }
}
