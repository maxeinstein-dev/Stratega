package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.UpdateWalletRequest;
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;
import br.com.maxsueleinstein.stratega.application.usecase.UpdateWalletUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;

import java.util.UUID;

public class UpdateWalletUseCaseImpl implements UpdateWalletUseCase {

    private final WalletRepository walletRepository;

    public UpdateWalletUseCaseImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse execute(UUID walletId, UUID userId, UpdateWalletRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));

        if (!wallet.getUserId().equals(userId)) {
            throw new ForbiddenException("Você não tem permissão para editar esta carteira");
        }

        wallet.updateName(request.name());
        Wallet savedWallet = walletRepository.save(wallet);

        return new WalletResponse(
                savedWallet.getId(),
                savedWallet.getName(),
                savedWallet.getBalance(),
                savedWallet.getUserId(),
                savedWallet.isActive()
        );
    }
}
