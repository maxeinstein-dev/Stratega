package br.com.maxsueleinstein.stratega.repository;

import br.com.maxsueleinstein.stratega.domain.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
