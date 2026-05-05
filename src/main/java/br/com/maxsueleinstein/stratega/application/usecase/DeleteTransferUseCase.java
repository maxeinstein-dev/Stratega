package br.com.maxsueleinstein.stratega.application.usecase;

import java.util.UUID;

public interface DeleteTransferUseCase {
    void execute(UUID transactionId, UUID userId);
}
