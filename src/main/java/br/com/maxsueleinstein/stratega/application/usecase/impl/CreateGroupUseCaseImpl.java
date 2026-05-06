package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.CreateGroupRequest;
import br.com.maxsueleinstein.stratega.application.usecase.CreateGroupUseCase;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroupMember;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;

public class CreateGroupUseCaseImpl implements CreateGroupUseCase {

    private final ExpenseGroupRepository repository;

    public CreateGroupUseCaseImpl(ExpenseGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExpenseGroup execute(CreateGroupRequest request) {
        ExpenseGroup group = new ExpenseGroup(null, request.name(), request.ownerId());
        
        // Add owner as the first member
        group.addMember(new ExpenseGroupMember(null, request.ownerName(), request.ownerId()));
        
        if (request.memberNames() != null) {
            for (String name : request.memberNames()) {
                group.addMember(new ExpenseGroupMember(null, name, null));
            }
        }
        
        return repository.save(group);
    }
}
