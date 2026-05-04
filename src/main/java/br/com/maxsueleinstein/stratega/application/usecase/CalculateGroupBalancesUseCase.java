package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.GroupBalancesResponse;
import java.util.UUID;

public interface CalculateGroupBalancesUseCase {
    GroupBalancesResponse execute(UUID groupId);
}
