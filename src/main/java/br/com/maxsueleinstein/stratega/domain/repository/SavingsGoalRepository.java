package br.com.maxsueleinstein.stratega.domain.repository;

import br.com.maxsueleinstein.stratega.domain.model.SavingsGoal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavingsGoalRepository {
    SavingsGoal save(SavingsGoal savingsGoal);
    Optional<SavingsGoal> findById(UUID id);
    List<SavingsGoal> findByUserId(UUID userId);
    void deleteById(UUID id);
}
