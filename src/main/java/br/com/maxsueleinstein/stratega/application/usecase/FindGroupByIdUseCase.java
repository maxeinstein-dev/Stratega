package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import java.util.UUID;

public interface FindGroupByIdUseCase {
    ExpenseGroup execute(UUID groupId, UUID userId);
}
