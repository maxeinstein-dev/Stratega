package br.com.maxsueleinstein.stratega.repository;

import br.com.maxsueleinstein.stratega.domain.entity.ExpenseShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long> {
}
