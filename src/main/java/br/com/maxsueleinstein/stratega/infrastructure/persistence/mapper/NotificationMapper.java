package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.Notification;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.NotificationEntity;

public class NotificationMapper {
    public static Notification toDomain(NotificationEntity entity) {
        if (entity == null) return null;
        return new Notification(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getType(),
                entity.isRead(),
                entity.getCreatedAt()
        );
    }

    public static NotificationEntity toEntity(Notification domain) {
        if (domain == null) return null;
        return NotificationEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .title(domain.getTitle())
                .message(domain.getMessage())
                .type(domain.getType())
                .isRead(domain.isRead())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
