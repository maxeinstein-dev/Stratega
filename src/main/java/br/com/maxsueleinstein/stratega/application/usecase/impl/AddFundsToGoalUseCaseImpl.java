package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.AddFundsToGoalRequest;
import br.com.maxsueleinstein.stratega.application.dto.SavingsGoalResponse;
import br.com.maxsueleinstein.stratega.application.usecase.AddFundsToGoalUseCase;
import br.com.maxsueleinstein.stratega.domain.model.SavingsGoal;
import br.com.maxsueleinstein.stratega.domain.repository.SavingsGoalRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class AddFundsToGoalUseCaseImpl implements AddFundsToGoalUseCase {

    private final SavingsGoalRepository savingsGoalRepository;

    public AddFundsToGoalUseCaseImpl(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    @Override
    public SavingsGoalResponse execute(UUID userId, UUID goalId, AddFundsToGoalRequest request) {
        SavingsGoal goal = savingsGoalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Objetivo de poupança não encontrado"));

        if (!goal.getUserId().equals(userId)) {
            throw new SecurityException("Sem permissão para acessar este objetivo");
        }

        goal.addFunds(request.amount());
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
