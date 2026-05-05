package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.MarkNotificationAsReadUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Notification;
import br.com.maxsueleinstein.stratega.domain.repository.NotificationRepository;

import java.util.UUID;

public class MarkNotificationAsReadUseCaseImpl implements MarkNotificationAsReadUseCase {

    private final NotificationRepository notificationRepository;

    public MarkNotificationAsReadUseCaseImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void execute(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada"));
        
        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
