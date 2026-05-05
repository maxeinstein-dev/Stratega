package br.com.maxsueleinstein.stratega.application.usecase;

import java.util.UUID;

public interface MarkNotificationAsReadUseCase {
    void execute(UUID notificationId);
}
