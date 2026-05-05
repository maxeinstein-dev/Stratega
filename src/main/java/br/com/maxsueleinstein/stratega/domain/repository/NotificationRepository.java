package br.com.maxsueleinstein.stratega.domain.repository;

import br.com.maxsueleinstein.stratega.domain.model.Notification;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> findByUserId(UUID userId);
    void deleteById(UUID id);
    java.util.Optional<Notification> findById(UUID id);
}
