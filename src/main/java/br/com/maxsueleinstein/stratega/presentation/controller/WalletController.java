package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.CreateWalletRequest;
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;
import br.com.maxsueleinstein.stratega.application.usecase.CreateWalletUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;

    public WalletController(CreateWalletUseCase createWalletUseCase) {
        this.createWalletUseCase = createWalletUseCase;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody CreateWalletRequest request) {
        WalletResponse response = createWalletUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
