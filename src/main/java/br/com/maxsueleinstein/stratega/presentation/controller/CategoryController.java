package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.CategoryResponse;
import br.com.maxsueleinstein.stratega.application.dto.CreateCategoryRequest;
import br.com.maxsueleinstein.stratega.application.dto.UpdateCategoryRequest;
import br.com.maxsueleinstein.stratega.application.usecase.CreateCategoryUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.DeleteCategoryUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.FindCategoriesByUserIdUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.UpdateCategoryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final FindCategoriesByUserIdUseCase findCategoriesByUserIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase,
                              FindCategoriesByUserIdUseCase findCategoriesByUserIdUseCase,
                              UpdateCategoryUseCase updateCategoryUseCase,
                              DeleteCategoryUseCase deleteCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.findCategoriesByUserIdUseCase = findCategoriesByUserIdUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        CategoryResponse response = createCategoryUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        UUID userId = getAuthenticatedUserId();
        List<CategoryResponse> categories = findCategoriesByUserIdUseCase.execute(userId);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable UUID id,
                                                         @RequestBody UpdateCategoryRequest request) {
        UUID userId = getAuthenticatedUserId();
        CategoryResponse response = updateCategoryUseCase.execute(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        deleteCategoryUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString((String) authentication.getPrincipal());
    }
}
