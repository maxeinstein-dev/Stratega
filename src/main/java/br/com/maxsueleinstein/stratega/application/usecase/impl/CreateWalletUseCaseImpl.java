package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.CreateWalletRequest;
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;
import br.com.maxsueleinstein.stratega.application.usecase.CreateWalletUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;

public class CreateWalletUseCaseImpl implements CreateWalletUseCase {

    private final WalletRepository walletRepository;

    public CreateWalletUseCaseImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse execute(CreateWalletRequest request) {
        boolean allowNegative = request.allowNegativeBalance() != null ? request.allowNegativeBalance() : true;
        Wallet wallet = new Wallet(null, request.name(), request.initialBalance(), request.userId(), request.currency(), true, allowNegative);
        Wallet savedWallet = walletRepository.save(wallet);
        return new WalletResponse(
                savedWallet.getId(),
                savedWallet.getName(),
                savedWallet.getBalance(),
                savedWallet.getUserId(),
                savedWallet.getCurrency(),
                savedWallet.isActive(),
                savedWallet.isAllowNegativeBalance()
        );
    }
}
