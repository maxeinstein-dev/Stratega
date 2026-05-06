package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.CreateSavingsGoalRequest;
import br.com.maxsueleinstein.stratega.application.dto.SavingsGoalResponse;
import br.com.maxsueleinstein.stratega.application.usecase.CreateSavingsGoalUseCase;
import br.com.maxsueleinstein.stratega.domain.model.SavingsGoal;
import br.com.maxsueleinstein.stratega.domain.repository.SavingsGoalRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class CreateSavingsGoalUseCaseImpl implements CreateSavingsGoalUseCase {

    private final SavingsGoalRepository savingsGoalRepository;

    public CreateSavingsGoalUseCaseImpl(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    @Override
    public SavingsGoalResponse execute(UUID userId, CreateSavingsGoalRequest request) {
        if (request.targetAmount() == null || request.targetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor alvo deve ser positivo");
        }
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("O nome do objetivo é obrigatório");
        }

        SavingsGoal goal = new SavingsGoal(
                null,
                userId,
                request.name(),
                request.targetAmount(),
                BigDecimal.ZERO,
                request.deadline(),
                null
        );

        SavingsGoal saved = savingsGoalRepository.save(goal);
        return toResponse(saved);
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
