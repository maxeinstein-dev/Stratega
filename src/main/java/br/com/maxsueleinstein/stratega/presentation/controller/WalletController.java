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
    private final br.com.maxsueleinstein.stratega.application.usecase.FindWalletsByUserIdUseCase findWalletsByUserIdUseCase;
 
    public WalletController(CreateWalletUseCase createWalletUseCase, br.com.maxsueleinstein.stratega.application.usecase.FindWalletsByUserIdUseCase findWalletsByUserIdUseCase) {
        this.createWalletUseCase = createWalletUseCase;
        this.findWalletsByUserIdUseCase = findWalletsByUserIdUseCase;
    }
 
    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody CreateWalletRequest request) {
        WalletResponse response = createWalletUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
 
    @GetMapping
    public ResponseEntity<java.util.List<WalletResponse>> getWallets() {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());
        
        java.util.List<WalletResponse> wallets = findWalletsByUserIdUseCase.execute(userId);
        return ResponseEntity.ok(wallets);
    }
}
