package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.SavingsGoalResponse;
import br.com.maxsueleinstein.stratega.application.usecase.ListSavingsGoalsUseCase;
import br.com.maxsueleinstein.stratega.domain.model.SavingsGoal;
import br.com.maxsueleinstein.stratega.domain.repository.SavingsGoalRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListSavingsGoalsUseCaseImpl implements ListSavingsGoalsUseCase {

    private final SavingsGoalRepository savingsGoalRepository;

    public ListSavingsGoalsUseCaseImpl(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    @Override
    public List<SavingsGoalResponse> execute(UUID userId) {
        return savingsGoalRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SavingsGoalResponse toResponse(SavingsGoal goal) {
        BigDecimal pct = BigDecimal.ZERO;
        if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
            pct = goal.getCurrentAmount().multiply(new BigDecimal("100"))
                    .divide(goal.getTargetAmount(), 2, RoundingMode.HALF_UP);
            if (pct.compareTo(new BigDecimal("100")) > 0) pct = new BigDecimal("100");
        }
        return new SavingsGoalResponse(
                goal.getId(),
                goal.getName(),
                goal.getTargetAmount(),
                goal.getCurrentAmount(),
                pct,
                goal.getDeadline()
        );
    }
}
