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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
@Tag(name = "Wallets", description = "Endpoints para gerenciamento de carteiras")
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
    @Operation(summary = "Criar nova carteira para o usuário autenticado")
    public ResponseEntity<WalletResponse> createWallet(
            @AuthenticationPrincipal User user,
            @RequestBody CreateWalletRequest request) {
        
        // Garante que a carteira seja criada para o usuário logado
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
    @Operation(summary = "Listar todas as carteiras do usuário autenticado")
    public ResponseEntity<List<WalletResponse>> getWallets(@AuthenticationPrincipal User user) {
        List<WalletResponse> wallets = findWalletsByUserIdUseCase.execute(user.getId());
        return ResponseEntity.ok(wallets);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de uma carteira")
    public ResponseEntity<WalletResponse> updateWallet(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user,
            @RequestBody UpdateWalletRequest request) {
        WalletResponse response = updateWalletUseCase.execute(id, user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma carteira (Soft Delete se houver transações)")
    public ResponseEntity<Void> deleteWallet(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        deleteWalletUseCase.execute(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
