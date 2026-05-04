package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.User;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword());
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return UserEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .build();
    }
}
