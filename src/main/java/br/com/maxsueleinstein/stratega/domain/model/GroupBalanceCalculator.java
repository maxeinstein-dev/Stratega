package br.com.maxsueleinstein.stratega.domain.model;

import br.com.maxsueleinstein.stratega.domain.model.split.Split;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GroupBalanceCalculator {

    public Map<UUID, BigDecimal> calculate(List<ExpenseGroupMember> members, List<GroupExpense> expenses) {
        Map<UUID, BigDecimal> balances = new HashMap<>();

        // Inicializar saldos com zero para todos os membros
        for (ExpenseGroupMember member : members) {
            balances.put(member.getId(), BigDecimal.ZERO.setScale(2));
        }

        for (GroupExpense expense : expenses) {
            UUID payerId = expense.getPaidBy().getId();

            // Adicionar o valor total pago ao saldo do pagador
            BigDecimal currentPayerBalance = balances.getOrDefault(payerId, BigDecimal.ZERO);
            balances.put(payerId, currentPayerBalance.add(expense.getTotalAmount()));

            // Subtrair a parte de cada membro (incluindo o pagador se ele estiver no split)
            for (Split split : expense.getSplits()) {
                UUID memberId = split.getMember().getId();
                BigDecimal currentMemberBalance = balances.getOrDefault(memberId, BigDecimal.ZERO);
                balances.put(memberId, currentMemberBalance.subtract(split.getAmount()));
            }
        }

        return balances;
    }

    public List<SuggestedTransfer> suggestTransfers(List<ExpenseGroupMember> members, Map<UUID, BigDecimal> balances) {
        List<SuggestedTransfer> transfers = new java.util.ArrayList<>();
        
        Map<UUID, BigDecimal> currentBalances = new HashMap<>(balances);
        
        List<ExpenseGroupMember> debtors = members.stream()
                .filter(m -> currentBalances.get(m.getId()).compareTo(BigDecimal.ZERO) < 0)
                .sorted((m1, m2) -> currentBalances.get(m1.getId()).compareTo(currentBalances.get(m2.getId()))) // Mais devedor primeiro
                .collect(java.util.stream.Collectors.toList());

        List<ExpenseGroupMember> creditors = members.stream()
                .filter(m -> currentBalances.get(m.getId()).compareTo(BigDecimal.ZERO) > 0)
                .sorted((m1, m2) -> currentBalances.get(m2.getId()).compareTo(currentBalances.get(m1.getId()))) // Mais credor primeiro
                .collect(java.util.stream.Collectors.toList());

        int dIdx = 0;
        int cIdx = 0;

        while (dIdx < debtors.size() && cIdx < creditors.size()) {
            ExpenseGroupMember debtor = debtors.get(dIdx);
            ExpenseGroupMember creditor = creditors.get(cIdx);

            BigDecimal debt = currentBalances.get(debtor.getId()).negate();
            BigDecimal credit = currentBalances.get(creditor.getId());

            BigDecimal transferAmount = debt.min(credit);

            if (transferAmount.compareTo(BigDecimal.ZERO) > 0) {
                transfers.add(new SuggestedTransfer(debtor, creditor, transferAmount));
                
                currentBalances.put(debtor.getId(), currentBalances.get(debtor.getId()).add(transferAmount));
                currentBalances.put(creditor.getId(), currentBalances.get(creditor.getId()).subtract(transferAmount));
            }

            if (currentBalances.get(debtor.getId()).compareTo(BigDecimal.ZERO) == 0) {
                dIdx++;
            }
            if (currentBalances.get(creditor.getId()).compareTo(BigDecimal.ZERO) == 0) {
                cIdx++;
            }
        }

        return transfers;
    }
}
