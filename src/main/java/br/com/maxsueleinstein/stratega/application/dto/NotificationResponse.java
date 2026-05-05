package br.com.maxsueleinstein.stratega.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String title,
        String message,
        String type,
        boolean isRead,
        LocalDateTime createdAt
) {}
