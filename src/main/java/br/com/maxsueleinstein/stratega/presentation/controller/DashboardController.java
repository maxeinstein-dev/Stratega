package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.DashboardSummaryResponse;
import br.com.maxsueleinstein.stratega.application.dto.HistoricalSummaryResponse;
import br.com.maxsueleinstein.stratega.application.dto.SpendingTrendResponse;
import br.com.maxsueleinstein.stratega.application.usecase.GetCategoryComparisonUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.GetDashboardSummaryUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.GetHistoricalSummaryUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.GetSpendingTrendUseCase;
import br.com.maxsueleinstein.stratega.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Financial summaries and reporting endpoints.")
public class DashboardController {

    private final GetDashboardSummaryUseCase getDashboardSummaryUseCase;
    private final GetSpendingTrendUseCase getSpendingTrendUseCase;
    private final GetCategoryComparisonUseCase getCategoryComparisonUseCase;
    private final GetHistoricalSummaryUseCase getHistoricalSummaryUseCase;

    public DashboardController(GetDashboardSummaryUseCase getDashboardSummaryUseCase,
            GetSpendingTrendUseCase getSpendingTrendUseCase,
            GetCategoryComparisonUseCase getCategoryComparisonUseCase,
            GetHistoricalSummaryUseCase getHistoricalSummaryUseCase) {
        this.getDashboardSummaryUseCase = getDashboardSummaryUseCase;
        this.getSpendingTrendUseCase = getSpendingTrendUseCase;
        this.getCategoryComparisonUseCase = getCategoryComparisonUseCase;
        this.getHistoricalSummaryUseCase = getHistoricalSummaryUseCase;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get the consolidated income, expense, and balance summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        DashboardSummaryResponse response = getDashboardSummaryUseCase.execute(user.getId(), month, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/trend")
    @Operation(summary = "Get the daily spending trend report")
    public ResponseEntity<SpendingTrendResponse> getSpendingTrend(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(getSpendingTrendUseCase.execute(user.getId(), month, year));
    }

    @GetMapping("/reports/comparison")
    @Operation(summary = "Compare spending by category against the previous month")
    public ResponseEntity<Map<String, GetCategoryComparisonUseCase.ComparisonData>> getComparison(
            @AuthenticationPrincipal User user,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(getCategoryComparisonUseCase.execute(user.getId(), month, year));
    }

    @GetMapping("/historical")
    @Operation(summary = "Get the historical income, expense, and savings summary")
    public ResponseEntity<HistoricalSummaryResponse> getHistorical(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "180") int days) {
        return ResponseEntity.ok(getHistoricalSummaryUseCase.execute(user.getId(), days));
    }
}
