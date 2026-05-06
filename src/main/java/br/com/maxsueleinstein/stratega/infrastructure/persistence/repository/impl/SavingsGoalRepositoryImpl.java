package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.impl;

import br.com.maxsueleinstein.stratega.domain.model.SavingsGoal;
import br.com.maxsueleinstein.stratega.domain.repository.SavingsGoalRepository;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.SavingsGoalEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataSavingsGoalRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SavingsGoalRepositoryImpl implements SavingsGoalRepository {

    private final SpringDataSavingsGoalRepository springDataRepository;

    public SavingsGoalRepositoryImpl(SpringDataSavingsGoalRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public SavingsGoal save(SavingsGoal savingsGoal) {
        SavingsGoalEntity entity = toEntity(savingsGoal);
        SavingsGoalEntity savedEntity = springDataRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<SavingsGoal> findById(UUID id) {
        return springDataRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<SavingsGoal> findByUserId(UUID userId) {
        return springDataRepository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }

    private SavingsGoalEntity toEntity(SavingsGoal domain) {
        return new SavingsGoalEntity(
                domain.getId(),
                domain.getUserId(),
                domain.getName(),
                domain.getTargetAmount(),
                domain.getCurrentAmount(),
                domain.getDeadline(),
                domain.getCreatedAt()
        );
    }

    private SavingsGoal toDomain(SavingsGoalEntity entity) {
        return new SavingsGoal(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getTargetAmount(),
                entity.getCurrentAmount(),
                entity.getDeadline(),
                entity.getCreatedAt()
        );
    }
}
