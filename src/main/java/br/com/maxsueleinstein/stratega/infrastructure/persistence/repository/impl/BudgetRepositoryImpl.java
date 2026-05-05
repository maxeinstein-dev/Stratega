package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.impl;

import br.com.maxsueleinstein.stratega.domain.model.Budget;
import br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.BudgetEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper.BudgetMapper;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataBudgetRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BudgetRepositoryImpl implements BudgetRepository {

    private final SpringDataBudgetRepository springDataBudgetRepository;

    public BudgetRepositoryImpl(SpringDataBudgetRepository springDataBudgetRepository) {
        this.springDataBudgetRepository = springDataBudgetRepository;
    }

    @Override
    public Budget save(Budget budget) {
        BudgetEntity entity = BudgetMapper.toEntity(budget);
        BudgetEntity saved = springDataBudgetRepository.save(entity);
        return BudgetMapper.toDomain(saved);
    }

    @Override
    public Optional<Budget> findByUserIdAndCategoryIdAndMonthAndYear(UUID userId, UUID categoryId, int month, int year) {
        return springDataBudgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(userId, categoryId, month, year)
                .map(BudgetMapper::toDomain);
    }

    @Override
    public List<Budget> findByUserIdAndMonthAndYear(UUID userId, int month, int year) {
        return springDataBudgetRepository.findByUserIdAndMonthAndYear(userId, month, year)
                .stream()
                .map(BudgetMapper::toDomain)
                .collect(Collectors.toList());
    }
}
