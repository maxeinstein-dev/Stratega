package br.com.maxsueleinstein.stratega.infrastructure.persistence.adapter;

import br.com.maxsueleinstein.stratega.domain.model.Notification;
import br.com.maxsueleinstein.stratega.domain.repository.NotificationRepository;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.NotificationEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper.NotificationMapper;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataNotificationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final SpringDataNotificationRepository repository;

    public NotificationRepositoryAdapter(SpringDataNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = NotificationMapper.toEntity(notification);
        NotificationEntity saved = repository.save(entity);
        return NotificationMapper.toDomain(saved);
    }

    @Override
    public List<Notification> findByUserId(UUID userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return repository.findById(id).map(NotificationMapper::toDomain);
    }
}
