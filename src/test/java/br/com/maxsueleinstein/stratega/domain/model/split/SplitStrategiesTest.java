package br.com.maxsueleinstein.stratega.domain.model.split;

import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroupMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SplitStrategiesTest {

    @Test
    @DisplayName("Deve dividir o valor uniformemente e lidar com o resto")
    void testUniformSplitStrategy() {
        ExpenseGroupMember member1 = new ExpenseGroupMember(null, "M1", null);
        ExpenseGroupMember member2 = new ExpenseGroupMember(null, "M2", null);
        ExpenseGroupMember member3 = new ExpenseGroupMember(null, "M3", null);

        List<Split> splits = new ArrayList<>();
        splits.add(new UniformSplit(member1));
        splits.add(new UniformSplit(member2));
        splits.add(new UniformSplit(member3));

        SplitStrategy strategy = new UniformSplitStrategy();
        strategy.calculateSplit(new BigDecimal("100.00"), splits);

        assertEquals(new BigDecimal("33.34"), splits.get(0).getAmount()); // Resto adicionado no primeiro
        assertEquals(new BigDecimal("33.33"), splits.get(1).getAmount());
        assertEquals(new BigDecimal("33.33"), splits.get(2).getAmount());
    }

    @Test
    @DisplayName("Deve validar a divisão exata e falhar se a soma for diferente do total")
    void testExactSplitStrategy() {
        ExpenseGroupMember member1 = new ExpenseGroupMember(null, "M1", null);
        ExpenseGroupMember member2 = new ExpenseGroupMember(null, "M2", null);

        List<Split> splits = new ArrayList<>();
        splits.add(new ExactSplit(member1, new BigDecimal("60.00")));
        splits.add(new ExactSplit(member2, new BigDecimal("40.00")));

        SplitStrategy strategy = new ExactSplitStrategy();
        strategy.calculateSplit(new BigDecimal("100.00"), splits);

        assertEquals(new BigDecimal("60.00"), splits.get(0).getAmount());
        assertEquals(new BigDecimal("40.00"), splits.get(1).getAmount());

        // Testar falha
        List<Split> invalidSplits = new ArrayList<>();
        invalidSplits.add(new ExactSplit(member1, new BigDecimal("60.00")));
        invalidSplits.add(new ExactSplit(member2, new BigDecimal("50.00")));

        assertThrows(IllegalArgumentException.class, () -> 
            strategy.calculateSplit(new BigDecimal("100.00"), invalidSplits)
        );
    }

    @Test
    @DisplayName("Deve calcular a divisão por porcentagem corretamente")
    void testPercentageSplitStrategy() {
        ExpenseGroupMember member1 = new ExpenseGroupMember(null, "M1", null);
        ExpenseGroupMember member2 = new ExpenseGroupMember(null, "M2", null);

        List<Split> splits = new ArrayList<>();
        splits.add(new PercentageSplit(member1, 60.0));
        splits.add(new PercentageSplit(member2, 40.0));

        SplitStrategy strategy = new PercentageSplitStrategy();
        strategy.calculateSplit(new BigDecimal("200.00"), splits);

        assertEquals(new BigDecimal("120.00"), splits.get(0).getAmount());
        assertEquals(new BigDecimal("80.00"), splits.get(1).getAmount());

        // Testar falha se não somar 100%
        List<Split> invalidSplits = new ArrayList<>();
        invalidSplits.add(new PercentageSplit(member1, 60.0));
        invalidSplits.add(new PercentageSplit(member2, 50.0));

        assertThrows(IllegalArgumentException.class, () -> 
            strategy.calculateSplit(new BigDecimal("200.00"), invalidSplits)
        );
    }

    @Test
    @DisplayName("Deve calcular a divisão por cotas (shares) corretamente")
    void testShareSplitStrategy() {
        ExpenseGroupMember member1 = new ExpenseGroupMember(null, "M1", null);
        ExpenseGroupMember member2 = new ExpenseGroupMember(null, "M2", null);

        List<Split> splits = new ArrayList<>();
        splits.add(new ShareSplit(member1, 3));
        splits.add(new ShareSplit(member2, 1)); // Total 4 cotas

        SplitStrategy strategy = new ShareSplitStrategy();
        strategy.calculateSplit(new BigDecimal("100.00"), splits);

        assertEquals(new BigDecimal("75.00"), splits.get(0).getAmount());
        assertEquals(new BigDecimal("25.00"), splits.get(1).getAmount());
    }
}
