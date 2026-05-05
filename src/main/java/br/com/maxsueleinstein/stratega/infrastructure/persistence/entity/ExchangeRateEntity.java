package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateEntity {
    @Id
    private String pair; // Ex: USD_BRL
    private BigDecimal rate;
    private LocalDateTime lastUpdated;
}
