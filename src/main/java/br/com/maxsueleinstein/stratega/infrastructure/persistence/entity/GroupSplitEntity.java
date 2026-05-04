package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "group_splits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupSplitEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private GroupMemberEntity member;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "percentage_value")
    private Double percentage;

    @Column(name = "shares_value")
    private Integer shares;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private GroupExpenseEntity expense;
}
