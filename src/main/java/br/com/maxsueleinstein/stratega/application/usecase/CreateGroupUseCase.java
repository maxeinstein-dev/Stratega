package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CreateGroupRequest;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;

public interface CreateGroupUseCase {
    ExpenseGroup execute(CreateGroupRequest request);
}
