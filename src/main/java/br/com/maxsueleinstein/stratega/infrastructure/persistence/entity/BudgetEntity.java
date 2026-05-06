package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import jakarta.persistence.Column;
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
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetEntity {
    @Id
    private UUID id;
    private UUID userId;
    private UUID categoryId;
    private BigDecimal amountLimit;
    @Column(name = "`month`")
    private int month;

    @Column(name = "`year`")
    private int year;
}
