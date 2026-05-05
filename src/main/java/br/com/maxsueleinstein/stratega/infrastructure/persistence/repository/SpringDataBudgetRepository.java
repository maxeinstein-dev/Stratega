package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataBudgetRepository extends JpaRepository<BudgetEntity, UUID> {
    Optional<BudgetEntity> findByUserIdAndCategoryIdAndMonthAndYear(UUID userId, UUID categoryId, int month, int year);
    List<BudgetEntity> findByUserIdAndMonthAndYear(UUID userId, int month, int year);
}
