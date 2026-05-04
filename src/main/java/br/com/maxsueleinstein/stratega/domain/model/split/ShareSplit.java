package br.com.maxsueleinstein.stratega.domain.model.split;

import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroupMember;

public class ShareSplit extends Split {

    private final int shares;

    public ShareSplit(ExpenseGroupMember member, int shares) {
        super(member);
        this.shares = shares;
    }

    public int getShares() {
        return shares;
    }
}
