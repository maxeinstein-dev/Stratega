package br.com.maxsueleinstein.stratega.application.usecase;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface GetCategoryComparisonUseCase {
    Map<String, ComparisonData> execute(UUID userId, int month, int year);

    record ComparisonData(
            BigDecimal currentMonth,
            BigDecimal previousMonth,
            BigDecimal differencePercentage
    ) {}
}
