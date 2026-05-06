package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.SavingsGoalResponse;
import java.util.List;
import java.util.UUID;

public interface ListSavingsGoalsUseCase {
    List<SavingsGoalResponse> execute(UUID userId);
}
