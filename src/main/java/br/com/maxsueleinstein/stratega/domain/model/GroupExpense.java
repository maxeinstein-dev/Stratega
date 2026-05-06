package br.com.maxsueleinstein.stratega.domain.model;

import br.com.maxsueleinstein.stratega.domain.model.split.Split;
import br.com.maxsueleinstein.stratega.domain.model.split.SplitStrategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class GroupExpense {
    private final UUID id;
    private final String description;
    private final BigDecimal totalAmount;
    private final ExpenseGroupMember paidBy;
    private final java.time.LocalDateTime date;
    private final List<Split> splits;
    private final String type; // "EXPENSE" ou "SETTLEMENT"

    public GroupExpense(UUID id, String description, BigDecimal totalAmount, ExpenseGroupMember paidBy, java.time.LocalDateTime date, List<Split> splits, SplitStrategy strategy, String type) {
        this.id = id != null ? id : UUID.randomUUID();
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.date = date != null ? date : java.time.LocalDateTime.now();
        this.splits = splits;
        this.type = type != null ? type : "EXPENSE";
        if (strategy != null) {
            strategy.calculateSplit(totalAmount, splits);
        }
    }

    public String getType() {
        return type;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public ExpenseGroupMember getPaidBy() {
        return paidBy;
    }

    public java.time.LocalDateTime getDate() {
        return date;
    }

    public List<Split> getSplits() {
        return splits;
    }
}
