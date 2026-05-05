package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataNotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
