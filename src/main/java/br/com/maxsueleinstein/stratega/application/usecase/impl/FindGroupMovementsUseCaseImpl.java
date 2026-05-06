package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.GroupMovementResponse;
import br.com.maxsueleinstein.stratega.application.usecase.FindGroupMovementsUseCase;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FindGroupMovementsUseCaseImpl implements FindGroupMovementsUseCase {

    private final ExpenseGroupRepository repository;

    public FindGroupMovementsUseCaseImpl(ExpenseGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GroupMovementResponse> execute(UUID groupId, UUID userId) {
        ExpenseGroup group = repository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado"));

        if (!group.isUserAllowed(userId)) {
            throw new ForbiddenException("Você não tem permissão para ver este grupo");
        }

        return group.getExpenses().stream()
                .map(e -> new GroupMovementResponse(
                        e.getId(),
                        e.getDescription(),
                        e.getTotalAmount(),
                        e.getType(),
                        e.getDate(),
                        e.getPaidBy().getName(),
                        e.getPaidBy().getId()
                ))
                .sorted(Comparator.comparing(GroupMovementResponse::date).reversed())
                .collect(Collectors.toList());
    }
}
