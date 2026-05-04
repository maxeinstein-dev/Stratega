package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.*;
import br.com.maxsueleinstein.stratega.domain.model.split.*;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.*;

import java.util.stream.Collectors;

public class ExpenseGroupMapper {

    public static ExpenseGroup toDomain(ExpenseGroupEntity entity) {
        if (entity == null) return null;
        
        ExpenseGroup group = new ExpenseGroup(entity.getId(), entity.getName(), entity.getOwnerId());
        
        if (entity.getMembers() != null) {
            entity.getMembers().forEach(m -> group.addMember(toDomain(m)));
        }
        
        if (entity.getExpenses() != null) {
            entity.getExpenses().forEach(e -> group.addExpense(toDomain(e, group.getMembers())));
        }
        
        return group;
    }

    public static ExpenseGroupMember toDomain(GroupMemberEntity entity) {
        return new ExpenseGroupMember(entity.getId(), entity.getName(), entity.getUserId());
    }

    public static GroupExpense toDomain(GroupExpenseEntity entity, java.util.List<ExpenseGroupMember> members) {
        ExpenseGroupMember paidBy = members.stream()
                .filter(m -> m.getId().equals(entity.getPaidBy().getId()))
                .findFirst()
                .orElse(null);

        SplitStrategy strategy = switch (entity.getSplitType()) {
            case "UNIFORM" -> new UniformSplitStrategy();
            case "EXACT" -> new ExactSplitStrategy();
            case "PERCENTAGE" -> new PercentageSplitStrategy();
            case "SHARE" -> new ShareSplitStrategy();
            default -> null;
        };

        java.util.List<Split> splits = entity.getSplits().stream()
                .map(s -> {
                    ExpenseGroupMember member = members.stream()
                            .filter(m -> m.getId().equals(s.getMember().getId()))
                            .findFirst()
                            .orElse(null);
                    
                    Split split = switch (entity.getSplitType()) {
                        case "UNIFORM" -> new UniformSplit(member);
                        case "EXACT" -> new ExactSplit(member, s.getAmount());
                        case "PERCENTAGE" -> new PercentageSplit(member, s.getPercentage());
                        case "SHARE" -> new ShareSplit(member, s.getShares());
                        default -> null;
                    };
                    if (split != null) split.setAmount(s.getAmount());
                    return split;
                })
                .collect(Collectors.toList());

        return new GroupExpense(entity.getId(), entity.getDescription(), entity.getTotalAmount(), paidBy, splits, strategy);
    }

    public static ExpenseGroupEntity toEntity(ExpenseGroup group) {
        if (group == null) return null;
        
        ExpenseGroupEntity entity = new ExpenseGroupEntity();
        entity.setId(group.getId());
        entity.setName(group.getName());
        entity.setOwnerId(group.getOwnerId());
        
        entity.setMembers(group.getMembers().stream()
                .map(m -> {
                    GroupMemberEntity me = new GroupMemberEntity(m.getId(), m.getName(), m.getUserId(), entity);
                    return me;
                })
                .collect(Collectors.toList()));
        
        entity.setExpenses(group.getExpenses().stream()
                .map(e -> toEntity(e, entity))
                .collect(Collectors.toList()));
        
        return entity;
    }

    private static GroupExpenseEntity toEntity(GroupExpense expense, ExpenseGroupEntity groupEntity) {
        GroupExpenseEntity entity = new GroupExpenseEntity();
        entity.setId(expense.getId());
        entity.setDescription(expense.getDescription());
        entity.setTotalAmount(expense.getTotalAmount());
        entity.setGroup(groupEntity);
        
        // Determinar o tipo de split e o pagador
        String splitType = "UNIFORM";
        if (!expense.getSplits().isEmpty()) {
            Split first = expense.getSplits().get(0);
            if (first instanceof ExactSplit) splitType = "EXACT";
            else if (first instanceof PercentageSplit) splitType = "PERCENTAGE";
            else if (first instanceof ShareSplit) splitType = "SHARE";
        }
        entity.setSplitType(splitType);

        GroupMemberEntity paidBy = groupEntity.getMembers().stream()
                .filter(m -> m.getId().equals(expense.getPaidBy().getId()))
                .findFirst()
                .orElse(null);
        entity.setPaidBy(paidBy);

        entity.setSplits(expense.getSplits().stream()
                .map(s -> {
                    GroupMemberEntity member = groupEntity.getMembers().stream()
                            .filter(m -> m.getId().equals(s.getMember().getId()))
                            .findFirst()
                            .orElse(null);
                    
                    GroupSplitEntity se = new GroupSplitEntity();
                    se.setId(java.util.UUID.randomUUID());
                    se.setMember(member);
                    se.setAmount(s.getAmount());
                    se.setExpense(entity);
                    
                    if (s instanceof PercentageSplit) se.setPercentage(((PercentageSplit) s).getPercentage());
                    if (s instanceof ShareSplit) se.setShares(((ShareSplit) s).getShares());
                    
                    return se;
                })
                .collect(Collectors.toList()));

        return entity;
    }
}
