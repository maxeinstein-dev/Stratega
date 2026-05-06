package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import br.com.maxsueleinstein.stratega.domain.model.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    private UUID id;
    private String description;
    private BigDecimal amount;
    private BigDecimal netAmount;
    private LocalDateTime date;
    
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    private UUID walletId;
    private UUID categoryId;
    private UUID linkedTransactionId;
    private UUID groupId;
}
