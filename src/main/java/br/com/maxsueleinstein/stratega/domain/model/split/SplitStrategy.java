package br.com.maxsueleinstein.stratega.domain.model.split;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public interface SplitStrategy {
    void calculateSplit(BigDecimal totalAmount, List<Split> splits);
}
