package br.com.maxsueleinstein.stratega.domain.repository;

import br.com.maxsueleinstein.stratega.domain.model.Budget;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository {
    Budget save(Budget budget);
    Optional<Budget> findByUserIdAndCategoryIdAndMonthAndYear(UUID userId, UUID categoryId, int month, int year);
    List<Budget> findByUserIdAndMonthAndYear(UUID userId, int month, int year);
}
