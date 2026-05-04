package br.com.maxsueleinstein.stratega.domain.model.split;

import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroupMember;

public class PercentageSplit extends Split {

    private final double percentage;

    public PercentageSplit(ExpenseGroupMember member, double percentage) {
        super(member);
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}
