package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.AddFundsToGoalRequest;
import br.com.maxsueleinstein.stratega.application.dto.CreateSavingsGoalRequest;
import br.com.maxsueleinstein.stratega.application.dto.SavingsGoalResponse;
import br.com.maxsueleinstein.stratega.application.usecase.AddFundsToGoalUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.CreateSavingsGoalUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.ListSavingsGoalsUseCase;
import br.com.maxsueleinstein.stratega.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@Tag(name = "Objetivos de Poupança", description = "Gerenciamento de metas financeiras")
public class SavingsGoalController {

    private final CreateSavingsGoalUseCase createSavingsGoalUseCase;
    private final ListSavingsGoalsUseCase listSavingsGoalsUseCase;
    private final AddFundsToGoalUseCase addFundsToGoalUseCase;

    public SavingsGoalController(CreateSavingsGoalUseCase createSavingsGoalUseCase,
                                 ListSavingsGoalsUseCase listSavingsGoalsUseCase,
                                 AddFundsToGoalUseCase addFundsToGoalUseCase) {
        this.createSavingsGoalUseCase = createSavingsGoalUseCase;
        this.listSavingsGoalsUseCase = listSavingsGoalsUseCase;
        this.addFundsToGoalUseCase = addFundsToGoalUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar novo objetivo de poupança")
    public ResponseEntity<SavingsGoalResponse> createGoal(
            @AuthenticationPrincipal User user,
            @RequestBody CreateSavingsGoalRequest request) {
        return ResponseEntity.ok(createSavingsGoalUseCase.execute(user.getId(), request));
    }

    @GetMapping
    @Operation(summary = "Listar objetivos de poupança")
    public ResponseEntity<List<SavingsGoalResponse>> listGoals(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(listSavingsGoalsUseCase.execute(user.getId()));
    }

    @PostMapping("/{goalId}/add-funds")
    @Operation(summary = "Adicionar fundos a um objetivo")
    public ResponseEntity<SavingsGoalResponse> addFunds(
            @AuthenticationPrincipal User user,
            @PathVariable UUID goalId,
            @RequestBody AddFundsToGoalRequest request) {
        return ResponseEntity.ok(addFundsToGoalUseCase.execute(user.getId(), goalId, request));
    }
}
