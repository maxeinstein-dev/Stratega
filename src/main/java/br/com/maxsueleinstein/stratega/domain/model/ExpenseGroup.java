package br.com.maxsueleinstein.stratega.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExpenseGroup {
    private final UUID id;
    private final String name;
    private final UUID ownerId;
    private final List<ExpenseGroupMember> members;
    private final List<GroupExpense> expenses;

    public ExpenseGroup(UUID id, String name, UUID ownerId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do grupo não pode estar em branco");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.ownerId = ownerId;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public List<ExpenseGroupMember> getMembers() {
        return members;
    }

    public List<GroupExpense> getExpenses() {
        return expenses;
    }

    public void addMember(ExpenseGroupMember member) {
        this.members.add(member);
    }

    public void addExpense(GroupExpense expense) {
        this.expenses.add(expense);
    }
}
