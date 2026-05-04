package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.Category;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.CategoryEntity;

public class CategoryMapper {

    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return new Category(entity.getId(), entity.getName(), entity.getType(), entity.getUserId());
    }

    public static CategoryEntity toEntity(Category domain) {
        if (domain == null) return null;
        return CategoryEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .type(domain.getType())
                .userId(domain.getUserId())
                .build();
    }
}
