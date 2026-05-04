package br.com.maxsueleinstein.stratega.domain.model.split;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ShareSplitStrategy implements SplitStrategy {

    @Override
    public void calculateSplit(BigDecimal totalAmount, List<Split> splits) {
        int totalShares = 0;

        for (Split split : splits) {
            if (!(split instanceof ShareSplit)) {
                throw new IllegalArgumentException("A estratégia por cotas requer objetos ShareSplit");
            }
            totalShares += ((ShareSplit) split).getShares();
        }

        if (totalShares == 0) {
            throw new IllegalArgumentException("O total de cotas não pode ser zero");
        }

        BigDecimal currentSum = BigDecimal.ZERO;

        for (Split split : splits) {
            ShareSplit shareSplit = (ShareSplit) split;
            BigDecimal amount = totalAmount.multiply(BigDecimal.valueOf(shareSplit.getShares()))
                    .divide(BigDecimal.valueOf(totalShares), 2, RoundingMode.DOWN);
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
