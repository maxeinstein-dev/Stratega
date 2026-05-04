package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CreateWalletRequest;
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;

public interface CreateWalletUseCase {
    WalletResponse execute(CreateWalletRequest request);
}
