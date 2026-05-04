package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.AddGroupExpenseRequest;
import br.com.maxsueleinstein.stratega.application.usecase.AddGroupExpenseUseCase;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroupMember;
import br.com.maxsueleinstein.stratega.domain.model.GroupExpense;
import br.com.maxsueleinstein.stratega.domain.model.split.*;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddGroupExpenseUseCaseImpl implements AddGroupExpenseUseCase {

    private final ExpenseGroupRepository repository;

    public AddGroupExpenseUseCaseImpl(ExpenseGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExpenseGroup execute(AddGroupExpenseRequest request) {
        ExpenseGroup group = repository.findById(request.groupId())
                .orElseThrow(() -> new br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException("Grupo não encontrado"));

        if (!group.isUserAllowed(request.requesterId())) {
            throw new br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException("Usuário não tem permissão para adicionar despesas neste grupo");
        }

        ExpenseGroupMember paidBy = group.getMembers().stream()
                .filter(m -> m.getId().equals(request.paidByMemberId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Membro pagador não encontrado no grupo"));

        List<Split> splits = new ArrayList<>();
        SplitStrategy strategy = switch (request.splitType()) {
            case "UNIFORM" -> {
                for (ExpenseGroupMember member : group.getMembers()) {
                    splits.add(new UniformSplit(member));
                }
                yield new UniformSplitStrategy();
            }
            case "EXACT" -> {
                request.splitValues().forEach((memberId, value) -> {
                    ExpenseGroupMember member = findMember(group, memberId);
                    splits.add(new ExactSplit(member, new BigDecimal(value.toString())));
                });
                yield new ExactSplitStrategy();
            }
            case "PERCENTAGE" -> {
                request.splitValues().forEach((memberId, value) -> {
                    ExpenseGroupMember member = findMember(group, memberId);
                    splits.add(new PercentageSplit(member, Double.parseDouble(value.toString())));
                });
                yield new PercentageSplitStrategy();
            }
            case "SHARE" -> {
                request.splitValues().forEach((memberId, value) -> {
                    ExpenseGroupMember member = findMember(group, memberId);
                    splits.add(new ShareSplit(member, Integer.parseInt(value.toString())));
                });
                yield new ShareSplitStrategy();
            }
            default -> throw new IllegalArgumentException("Tipo de divisão inválido");
        };

        GroupExpense expense = new GroupExpense(null, request.description(), request.amount(), paidBy, request.date(), splits, strategy);
        group.addExpense(expense);

        return repository.save(group);
    }

    private ExpenseGroupMember findMember(ExpenseGroup group, UUID memberId) {
        return group.getMembers().stream()
                .filter(m -> m.getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Membro " + memberId + " não encontrado no grupo"));
    }
}
