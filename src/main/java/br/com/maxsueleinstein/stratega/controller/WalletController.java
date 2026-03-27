package br.com.maxsueleinstein.stratega.controller;

import br.com.maxsueleinstein.stratega.domain.entity.Wallet;
import br.com.maxsueleinstein.stratega.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallets(@RequestBody Wallet user) {
        Wallet saved = walletService.createWallet(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @GetMapping
    public void getWallets() {

    }
}
