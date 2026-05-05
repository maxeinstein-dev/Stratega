package br.com.maxsueleinstein.stratega.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    private final UUID id;
    private final UUID userId;
    private final String title;
    private final String message;
    private final String type;
    private boolean read;
    private final LocalDateTime createdAt;

    public Notification(UUID id, UUID userId, String title, String message, String type, boolean read, LocalDateTime createdAt) {
        this.id = id != null ? id : UUID.randomUUID();
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.read = read;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public void markAsRead() {
        this.read = true;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
