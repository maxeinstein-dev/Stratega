package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CreateSavingsGoalRequest;
import br.com.maxsueleinstein.stratega.application.dto.SavingsGoalResponse;

import java.util.UUID;

public interface CreateSavingsGoalUseCase {
    SavingsGoalResponse execute(UUID userId, CreateSavingsGoalRequest request);
}
