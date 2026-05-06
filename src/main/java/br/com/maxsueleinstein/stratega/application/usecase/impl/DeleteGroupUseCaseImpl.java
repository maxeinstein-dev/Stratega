package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.DeleteGroupUseCase;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;
import java.util.UUID;

public class DeleteGroupUseCaseImpl implements DeleteGroupUseCase {

    private final ExpenseGroupRepository repository;

    public DeleteGroupUseCaseImpl(ExpenseGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(UUID groupId, UUID userId) {
        ExpenseGroup group = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));

        if (!group.getOwnerId().equals(userId)) {
            throw new IllegalArgumentException("Apenas o dono pode excluir o grupo");
        }

        repository.deleteById(groupId);
    }
}
