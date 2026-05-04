package br.com.maxsueleinstein.stratega.domain.model.split;

import java.math.BigDecimal;
import java.util.List;

public class ExactSplitStrategy implements SplitStrategy {

    @Override
    public void calculateSplit(BigDecimal totalAmount, List<Split> splits) {
        BigDecimal sum = BigDecimal.ZERO;
        
        for (Split split : splits) {
            if (!(split instanceof ExactSplit)) {
                throw new IllegalArgumentException("A estratégia exata requer objetos ExactSplit");
            }
            sum = sum.add(split.getAmount());
        }

        if (sum.compareTo(totalAmount) != 0) {
            throw new IllegalArgumentException("A soma das divisões exatas (" + sum + ") não é igual ao total da despesa (" + totalAmount + ")");
        }
    }
}
