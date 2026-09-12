package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.CreateWalletRequest;
import br.com.maxsueleinstein.stratega.application.dto.UpdateWalletRequest;
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;
import br.com.maxsueleinstein.stratega.application.usecase.CreateWalletUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.DeleteWalletUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.FindWalletsByUserIdUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.UpdateWalletUseCase;
import br.com.maxsueleinstein.stratega.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
@Tag(name = "Wallets", description = "Wallet management endpoints.")
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final FindWalletsByUserIdUseCase findWalletsByUserIdUseCase;
    private final UpdateWalletUseCase updateWalletUseCase;
    private final DeleteWalletUseCase deleteWalletUseCase;

    public WalletController(CreateWalletUseCase createWalletUseCase,
            FindWalletsByUserIdUseCase findWalletsByUserIdUseCase,
            UpdateWalletUseCase updateWalletUseCase,
            DeleteWalletUseCase deleteWalletUseCase) {
        this.createWalletUseCase = createWalletUseCase;
        this.findWalletsByUserIdUseCase = findWalletsByUserIdUseCase;
        this.updateWalletUseCase = updateWalletUseCase;
        this.deleteWalletUseCase = deleteWalletUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a wallet for the authenticated user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Create a main wallet",
                            value = """
                                    {
                                      "name": "Main Wallet",
                                      "initialBalance": 2500.00,
                                      "userId": null,
                                      "currency": "BRL",
                                      "allowNegativeBalance": false
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<WalletResponse> createWallet(
            @AuthenticationPrincipal User user,
            @RequestBody CreateWalletRequest request) {
        
        // Always bind the wallet to the authenticated user.
        CreateWalletRequest authenticatedRequest = new CreateWalletRequest(
                request.name(), 
                request.initialBalance(), 
                user.getId(), 
                request.currency(),
                request.allowNegativeBalance()
        );
        
        WalletResponse response = createWalletUseCase.execute(authenticatedRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List the authenticated user's wallets")
    public ResponseEntity<List<WalletResponse>> getWallets(@AuthenticationPrincipal User user) {
        List<WalletResponse> wallets = findWalletsByUserIdUseCase.execute(user.getId());
        return ResponseEntity.ok(wallets);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a wallet")
    public ResponseEntity<WalletResponse> updateWallet(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user,
            @RequestBody UpdateWalletRequest request) {
        WalletResponse response = updateWalletUseCase.execute(id, user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a wallet")
    public ResponseEntity<Void> deleteWallet(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        deleteWalletUseCase.execute(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
