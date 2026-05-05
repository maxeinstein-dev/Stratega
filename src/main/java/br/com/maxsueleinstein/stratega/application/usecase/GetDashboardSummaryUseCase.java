package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.DashboardSummaryResponse;

import java.util.UUID;

public interface GetDashboardSummaryUseCase {
    DashboardSummaryResponse execute(UUID userId, Integer month, Integer year);
}
