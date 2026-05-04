package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.GroupBalancesResponse;
import br.com.maxsueleinstein.stratega.application.usecase.CalculateGroupBalancesUseCase;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.model.GroupBalanceCalculator;
import br.com.maxsueleinstein.stratega.domain.model.SuggestedTransfer;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CalculateGroupBalancesUseCaseImpl implements CalculateGroupBalancesUseCase {

    private final ExpenseGroupRepository repository;
    private final GroupBalanceCalculator calculator;

    public CalculateGroupBalancesUseCaseImpl(ExpenseGroupRepository repository) {
        this.repository = repository;
        this.calculator = new GroupBalanceCalculator();
    }

    @Override
    public GroupBalancesResponse execute(UUID groupId, UUID requesterId) {
        ExpenseGroup group = repository.findById(groupId)
                .orElseThrow(() -> new br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException("Grupo não encontrado"));

        if (!group.isUserAllowed(requesterId)) {
            throw new br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException("Usuário não tem permissão para ver os saldos deste grupo");
        }

        Map<UUID, BigDecimal> balances = calculator.calculate(group.getMembers(), group.getExpenses());
        List<SuggestedTransfer> transfers = calculator.suggestTransfers(group.getMembers(), balances);

        return new GroupBalancesResponse(
            group.getId(),
            group.getName(),
            balances,
            transfers
        );
    }
}
