package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.BudgetResponse;

import java.util.List;
import java.util.UUID;

public interface GetBudgetsUseCase {
    List<BudgetResponse> execute(UUID userId, int month, int year);
}
