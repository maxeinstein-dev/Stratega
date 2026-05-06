package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record HistoricalSummaryResponse(
        List<PeriodSummary> periods
) {
    public record PeriodSummary(
            String periodLabel,
            BigDecimal income,
            BigDecimal expense,
            BigDecimal savings
    ) {}
}
