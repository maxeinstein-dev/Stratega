package br.com.maxsueleinstein.stratega.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "expense_participants")
public class ExpenseParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Expense expense;
    private String participantName;
    @ManyToOne
    private User user;

}
