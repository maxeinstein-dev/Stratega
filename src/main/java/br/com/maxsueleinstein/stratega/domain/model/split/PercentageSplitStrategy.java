package br.com.maxsueleinstein.stratega.domain.model.split;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public void calculateSplit(BigDecimal totalAmount, List<Split> splits) {
        double totalPercentage = 0;

        for (Split split : splits) {
            if (!(split instanceof PercentageSplit)) {
                throw new IllegalArgumentException("A estratégia por porcentagem requer objetos PercentageSplit");
            }
            totalPercentage += ((PercentageSplit) split).getPercentage();
        }

        if (Math.abs(100.0 - totalPercentage) > 0.001) {
            throw new IllegalArgumentException("A soma das porcentagens (" + totalPercentage + "%) deve ser 100%");
        }

        BigDecimal currentSum = BigDecimal.ZERO;

        for (Split split : splits) {
            PercentageSplit percentSplit = (PercentageSplit) split;
            BigDecimal amount = totalAmount.multiply(BigDecimal.valueOf(percentSplit.getPercentage() / 100.0))
                                            .setScale(2, RoundingMode.DOWN);
            split.setAmount(amount);
            currentSum = currentSum.add(amount);
        }

        BigDecimal remainder = totalAmount.subtract(currentSum);
        if (remainder.compareTo(BigDecimal.ZERO) > 0 && !splits.isEmpty()) {
            Split firstSplit = splits.get(0);
            firstSplit.setAmount(firstSplit.getAmount().add(remainder));
        }
    }
}
