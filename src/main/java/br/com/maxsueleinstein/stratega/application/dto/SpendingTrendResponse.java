package br.com.maxsueleinstein.stratega.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SpendingTrendResponse(
        List<DataPoint> trend
) {
    public record DataPoint(
            LocalDate date,
            BigDecimal amount
    ) {}
}
