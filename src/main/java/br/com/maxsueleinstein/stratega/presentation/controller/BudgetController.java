package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.BudgetRequest;
import br.com.maxsueleinstein.stratega.application.dto.BudgetResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetBudgetsUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.SetBudgetUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final SetBudgetUseCase setBudgetUseCase;
    private final GetBudgetsUseCase getBudgetsUseCase;

    public BudgetController(SetBudgetUseCase setBudgetUseCase, GetBudgetsUseCase getBudgetsUseCase) {
        this.setBudgetUseCase = setBudgetUseCase;
        this.getBudgetsUseCase = getBudgetsUseCase;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> setBudget(@RequestBody BudgetRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString((String) authentication.getPrincipal());

        BudgetResponse response = setBudgetUseCase.execute(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            @RequestParam int month,
            @RequestParam int year) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString((String) authentication.getPrincipal());

        List<BudgetResponse> response = getBudgetsUseCase.execute(userId, month, year);
        return ResponseEntity.ok(response);
    }
}
