package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.GroupMovementResponse;
import java.util.List;
import java.util.UUID;

public interface FindGroupMovementsUseCase {
    List<GroupMovementResponse> execute(UUID groupId, UUID userId);
}
