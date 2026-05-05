package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.SettleDebtRequest;
import java.util.UUID;

public interface SettleGroupDebtUseCase {
    void execute(UUID groupId, UUID userId, SettleDebtRequest request);
}
