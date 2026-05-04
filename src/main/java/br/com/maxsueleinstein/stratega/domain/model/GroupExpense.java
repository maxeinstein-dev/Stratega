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
    private final List<Split> splits;

    public GroupExpense(UUID id, String description, BigDecimal totalAmount, ExpenseGroupMember paidBy, List<Split> splits, SplitStrategy strategy) {
        this.id = id != null ? id : UUID.randomUUID();
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.splits = splits;
        strategy.calculateSplit(totalAmount, splits);
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

    public List<Split> getSplits() {
        return splits;
    }
}
