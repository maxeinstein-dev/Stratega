package br.com.maxsueleinstein.stratega.domain.model.split;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class UniformSplitStrategy implements SplitStrategy {

    @Override
    public void calculateSplit(BigDecimal totalAmount, List<Split> splits) {
        if (splits.isEmpty()) return;

        BigDecimal count = BigDecimal.valueOf(splits.size());
        BigDecimal splitAmount = totalAmount.divide(count, 2, RoundingMode.DOWN);

        BigDecimal currentSum = BigDecimal.ZERO;

        for (Split split : splits) {
            if (!(split instanceof UniformSplit)) {
                throw new IllegalArgumentException("A estratégia uniforme requer objetos UniformSplit");
            }
            split.setAmount(splitAmount);
            currentSum = currentSum.add(splitAmount);
        }

        BigDecimal remainder = totalAmount.subtract(currentSum);
        if (remainder.compareTo(BigDecimal.ZERO) > 0) {
            Split firstSplit = splits.get(0);
            firstSplit.setAmount(firstSplit.getAmount().add(remainder));
        }
    }
}
