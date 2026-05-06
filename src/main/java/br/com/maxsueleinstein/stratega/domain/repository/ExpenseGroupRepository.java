package br.com.maxsueleinstein.stratega.domain.repository;

import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseGroupRepository {
    ExpenseGroup save(ExpenseGroup group);
    Optional<ExpenseGroup> findById(UUID id);
    java.util.List<ExpenseGroup> findByUserId(UUID userId);
    void deleteById(UUID id);
}
