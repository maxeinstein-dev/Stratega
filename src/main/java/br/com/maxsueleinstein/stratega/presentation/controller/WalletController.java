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
    private final br.com.maxsueleinstein.stratega.application.usecase.UpdateWalletUseCase updateWalletUseCase;
    private final br.com.maxsueleinstein.stratega.application.usecase.DeleteWalletUseCase deleteWalletUseCase;

    public WalletController(CreateWalletUseCase createWalletUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.FindWalletsByUserIdUseCase findWalletsByUserIdUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.UpdateWalletUseCase updateWalletUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.DeleteWalletUseCase deleteWalletUseCase) {
        this.createWalletUseCase = createWalletUseCase;
        this.findWalletsByUserIdUseCase = findWalletsByUserIdUseCase;
        this.updateWalletUseCase = updateWalletUseCase;
        this.deleteWalletUseCase = deleteWalletUseCase;
    }
 
    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody CreateWalletRequest request) {
        WalletResponse response = createWalletUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
 
    @GetMapping
    public ResponseEntity<java.util.List<WalletResponse>> getWallets() {
        java.util.UUID userId = getAuthenticatedUserId();
        java.util.List<WalletResponse> wallets = findWalletsByUserIdUseCase.execute(userId);
        return ResponseEntity.ok(wallets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletResponse> updateWallet(@PathVariable java.util.UUID id,
            @RequestBody br.com.maxsueleinstein.stratega.application.dto.UpdateWalletRequest request) {
        java.util.UUID userId = getAuthenticatedUserId();
        WalletResponse response = updateWalletUseCase.execute(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable java.util.UUID id) {
        java.util.UUID userId = getAuthenticatedUserId();
        deleteWalletUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }

    private java.util.UUID getAuthenticatedUserId() {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        return java.util.UUID.fromString((String) authentication.getPrincipal());
    }
}
