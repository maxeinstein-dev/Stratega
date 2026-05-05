package br.com.maxsueleinstein.stratega.application.event;

import br.com.maxsueleinstein.stratega.domain.event.BudgetExceededEvent;
import br.com.maxsueleinstein.stratega.domain.model.Notification;
import br.com.maxsueleinstein.stratega.domain.repository.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;

    public NotificationEventListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @EventListener
    public void handleBudgetExceeded(BudgetExceededEvent event) {
        String title = "Limite de Orçamento Atingido";
        String message = String.format("Atenção! Seus gastos na categoria '%s' atingiram R$ %.2f, superando o limite de R$ %.2f.",
                event.categoryName(), event.currentSpent(), event.limit());
        
        Notification notification = new Notification(
                null,
                event.userId(),
                title,
                message,
                "BUDGET_EXCEEDED",
                false,
                null
        );
        
        notificationRepository.save(notification);
    }
}
