package br.com.maxsueleinstein.stratega.application.usecase.impl;
 
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;
import br.com.maxsueleinstein.stratega.application.usecase.FindWalletsByUserIdUseCase;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
 
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
 
public class FindWalletsByUserIdUseCaseImpl implements FindWalletsByUserIdUseCase {
 
    private final WalletRepository walletRepository;
 
    public FindWalletsByUserIdUseCaseImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }
 
    @Override
    public List<WalletResponse> execute(UUID userId) {
        return walletRepository.findByUserId(userId).stream()
                .filter(br.com.maxsueleinstein.stratega.domain.model.Wallet::isActive)
                .map(wallet -> new WalletResponse(
                        wallet.getId(),
                        wallet.getName(),
                        wallet.getBalance(),
                        wallet.getUserId(),
                        wallet.getCurrency(),
                        wallet.isActive()
                ))
                .collect(Collectors.toList());
    }
}
