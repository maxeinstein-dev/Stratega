package br.com.maxsueleinstein.stratega.application.usecase;

import java.util.UUID;

public interface DeleteWalletUseCase {
    void execute(UUID walletId, UUID userId);
}
