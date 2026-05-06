package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "group_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupExpenseEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by_id", nullable = false)
    private GroupMemberEntity paidBy;

    @Column(name = "split_type", nullable = false)
    private String splitType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private ExpenseGroupEntity group;

    @Column(name = "date_time")
    private java.time.LocalDateTime date;

    @Column(name = "movement_type", nullable = false)
    private String type; // "EXPENSE" ou "SETTLEMENT"

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupSplitEntity> splits = new ArrayList<>();
}
