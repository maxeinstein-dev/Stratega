package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "group_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private ExpenseGroupEntity group;
}
