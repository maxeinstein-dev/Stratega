package br.com.maxsueleinstein.stratega.domain.model.split;

import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroupMember;

import java.math.BigDecimal;

public abstract class Split {

    private final ExpenseGroupMember member;
    private BigDecimal amount;

    public Split(ExpenseGroupMember member) {
        this.member = member;
        this.amount = BigDecimal.ZERO;
    }

    public Split(ExpenseGroupMember member, BigDecimal amount) {
        this.member = member;
        this.amount = amount != null ? amount : BigDecimal.ZERO;
    }

    public ExpenseGroupMember getMember() {
        return member;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
