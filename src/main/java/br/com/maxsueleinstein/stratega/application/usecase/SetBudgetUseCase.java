package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.BudgetRequest;
import br.com.maxsueleinstein.stratega.application.dto.BudgetResponse;

import java.util.UUID;

public interface SetBudgetUseCase {
    BudgetResponse execute(UUID userId, BudgetRequest request);
}
