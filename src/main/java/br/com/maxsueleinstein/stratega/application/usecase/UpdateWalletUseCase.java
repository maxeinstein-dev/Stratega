package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.UpdateWalletRequest;
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;

import java.util.UUID;

public interface UpdateWalletUseCase {
    WalletResponse execute(UUID walletId, UUID userId, UpdateWalletRequest request);
}
