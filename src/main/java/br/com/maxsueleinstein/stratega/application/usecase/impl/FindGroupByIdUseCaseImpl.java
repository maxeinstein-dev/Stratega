package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.FindGroupByIdUseCase;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;
import java.util.UUID;

public class FindGroupByIdUseCaseImpl implements FindGroupByIdUseCase {

    private final ExpenseGroupRepository repository;

    public FindGroupByIdUseCaseImpl(ExpenseGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExpenseGroup execute(UUID groupId, UUID userId) {
        ExpenseGroup group = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        if (!group.isUserAllowed(userId)) {
            throw new IllegalArgumentException("Acesso negado ao grupo");
        }

        return group;
    }
}
