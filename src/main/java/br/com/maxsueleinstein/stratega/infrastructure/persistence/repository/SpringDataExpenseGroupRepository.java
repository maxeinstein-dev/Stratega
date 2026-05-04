package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.ExpenseGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface SpringDataExpenseGroupRepository extends JpaRepository<ExpenseGroupEntity, UUID> {
    
    @Query("SELECT DISTINCT g FROM ExpenseGroupEntity g LEFT JOIN g.members m WHERE g.ownerId = :userId OR m.userId = :userId")
    List<ExpenseGroupEntity> findByUserId(@Param("userId") UUID userId);
}
