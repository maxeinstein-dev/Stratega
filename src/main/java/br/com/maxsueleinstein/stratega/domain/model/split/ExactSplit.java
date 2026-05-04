package br.com.maxsueleinstein.stratega.domain.model.split;

import java.math.BigDecimal;

public class ExactSplit extends Split {

    public ExactSplit(br.com.maxsueleinstein.stratega.domain.model.ExpenseGroupMember member, BigDecimal amount) {
        super(member, amount);
    }
}
