package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {
    @Id
    private UUID id;
    private String name;
    private BigDecimal balance;
    private UUID userId;
}
