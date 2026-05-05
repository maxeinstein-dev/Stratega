package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.Budget;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.BudgetEntity;

public class BudgetMapper {

    public static Budget toDomain(BudgetEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Budget(
                entity.getId(),
                entity.getUserId(),
                entity.getCategoryId(),
                entity.getAmountLimit(),
                entity.getMonth(),
                entity.getYear()
        );
    }

    public static BudgetEntity toEntity(Budget domain) {
        if (domain == null) {
            return null;
        }
        return BudgetEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .categoryId(domain.getCategoryId())
                .amountLimit(domain.getAmountLimit())
                .month(domain.getMonth())
                .year(domain.getYear())
                .build();
    }
}
