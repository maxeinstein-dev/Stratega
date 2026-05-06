package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.BudgetRequest;
import br.com.maxsueleinstein.stratega.application.dto.BudgetResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetBudgetsUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.SetBudgetUseCase;
import br.com.maxsueleinstein.stratega.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@Tag(name = "Budgets", description = "Endpoints para gerenciamento de metas financeiras")
public class BudgetController {

    private final SetBudgetUseCase setBudgetUseCase;
    private final GetBudgetsUseCase getBudgetsUseCase;

    public BudgetController(SetBudgetUseCase setBudgetUseCase, GetBudgetsUseCase getBudgetsUseCase) {
        this.setBudgetUseCase = setBudgetUseCase;
        this.getBudgetsUseCase = getBudgetsUseCase;
    }

    @PostMapping
    @Operation(summary = "Definir ou atualizar meta de gastos para uma categoria")
    public ResponseEntity<BudgetResponse> setBudget(
            @AuthenticationPrincipal User user,
            @RequestBody BudgetRequest request) {
        BudgetResponse response = setBudgetUseCase.execute(user.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar metas de gastos do mês")
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year) {
        List<BudgetResponse> response = getBudgetsUseCase.execute(user.getId(), month, year);
        return ResponseEntity.ok(response);
    }
}
