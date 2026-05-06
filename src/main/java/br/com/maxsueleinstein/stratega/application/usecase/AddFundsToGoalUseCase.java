package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.AddFundsToGoalRequest;
import br.com.maxsueleinstein.stratega.application.dto.SavingsGoalResponse;

import java.util.UUID;

public interface AddFundsToGoalUseCase {
    SavingsGoalResponse execute(UUID userId, UUID goalId, AddFundsToGoalRequest request);
}
