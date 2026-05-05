package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.NotificationResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetNotificationsUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.MarkNotificationAsReadUseCase;
import br.com.maxsueleinstein.stratega.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Endpoints para gerenciamento de notificações")
public class NotificationController {

    private final GetNotificationsUseCase getNotificationsUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;

    public NotificationController(GetNotificationsUseCase getNotificationsUseCase,
                                MarkNotificationAsReadUseCase markNotificationAsReadUseCase) {
        this.getNotificationsUseCase = getNotificationsUseCase;
        this.markNotificationAsReadUseCase = markNotificationAsReadUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar notificações do usuário autenticado")
    public ResponseEntity<List<NotificationResponse>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(getNotificationsUseCase.execute(user.getId()));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Marcar uma notificação como lida")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        markNotificationAsReadUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
