package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.NotificationResponse;
import java.util.List;
import java.util.UUID;

public interface GetNotificationsUseCase {
    List<NotificationResponse> execute(UUID userId);
}
