package br.com.maxsueleinstein.stratega.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_shares")
public class ExpenseShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Expense expense;
    @ManyToOne
    private ExpenseParticipant participant;
    private BigDecimal shareAmout;
    private BigDecimal paidAmount;

}
