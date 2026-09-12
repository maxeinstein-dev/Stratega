package br.com.maxsueleinstein.stratega.presentation.controller;

import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Instant;
import java.util.Map;

@RestController
@Tag(name = "System", description = "Service status and health endpoints.")
public class StatusController {

    @GetMapping("/")
    @Operation(summary = "Redirect to Swagger UI")
    public RedirectView root() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping("/health")
    @Operation(summary = "Check service health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "timestamp", Instant.now().toString()
        ));
    }
}
