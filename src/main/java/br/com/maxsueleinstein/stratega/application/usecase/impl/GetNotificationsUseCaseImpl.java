package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.NotificationResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetNotificationsUseCase;
import br.com.maxsueleinstein.stratega.domain.repository.NotificationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetNotificationsUseCaseImpl implements GetNotificationsUseCase {

    private final NotificationRepository notificationRepository;

    public GetNotificationsUseCaseImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<NotificationResponse> execute(UUID userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(n -> new NotificationResponse(
                        n.getId(),
                        n.getTitle(),
                        n.getMessage(),
                        n.getType(),
                        n.isRead(),
                        n.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
