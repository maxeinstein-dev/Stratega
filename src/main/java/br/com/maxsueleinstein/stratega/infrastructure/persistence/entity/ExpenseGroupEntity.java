package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "expense_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseGroupEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMemberEntity> members = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupExpenseEntity> expenses = new ArrayList<>();
}
