package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.HistoricalSummaryResponse;
import java.util.UUID;

public interface GetHistoricalSummaryUseCase {
    HistoricalSummaryResponse execute(UUID userId, int days);
}
