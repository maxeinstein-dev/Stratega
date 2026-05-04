package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.ExpenseGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataExpenseGroupRepository extends JpaRepository<ExpenseGroupEntity, UUID> {
}
