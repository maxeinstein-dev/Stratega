package br.com.maxsueleinstein.stratega.service;

import br.com.maxsueleinstein.stratega.domain.entity.User;
import br.com.maxsueleinstein.stratega.domain.entity.Wallet;
import br.com.maxsueleinstein.stratega.repository.UserRepository;
import br.com.maxsueleinstein.stratega.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }


    public Wallet createWallet(Wallet wallet) {

        Long userId = wallet.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        wallet.setUser(user);
        return walletRepository.save(wallet);
    }
}
