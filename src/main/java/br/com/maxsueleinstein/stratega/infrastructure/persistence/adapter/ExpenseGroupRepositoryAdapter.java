package br.com.maxsueleinstein.stratega.infrastructure.persistence.adapter;

import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.ExpenseGroupEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper.ExpenseGroupMapper;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataExpenseGroupRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ExpenseGroupRepositoryAdapter implements ExpenseGroupRepository {

    private final SpringDataExpenseGroupRepository repository;

    public ExpenseGroupRepositoryAdapter(SpringDataExpenseGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExpenseGroup save(ExpenseGroup group) {
        ExpenseGroupEntity entity = ExpenseGroupMapper.toEntity(group);
        ExpenseGroupEntity saved = repository.save(entity);
        return ExpenseGroupMapper.toDomain(saved);
    }

    @Override
    public Optional<ExpenseGroup> findById(UUID id) {
        return repository.findById(id).map(ExpenseGroupMapper::toDomain);
    }

    @Override
    public java.util.List<ExpenseGroup> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(ExpenseGroupMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
