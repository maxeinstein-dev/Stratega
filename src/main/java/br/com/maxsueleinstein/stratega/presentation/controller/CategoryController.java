package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.CategoryResponse;
import br.com.maxsueleinstein.stratega.application.dto.CreateCategoryRequest;
import br.com.maxsueleinstein.stratega.application.dto.UpdateCategoryRequest;
import br.com.maxsueleinstein.stratega.application.usecase.CreateCategoryUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.DeleteCategoryUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.FindCategoriesByUserIdUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.UpdateCategoryUseCase;
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
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Transaction category management endpoints.")
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
    @Operation(summary = "Create a custom transaction category")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Create an income category",
                            value = """
                                    {
                                      "name": "Freelance",
                                      "type": "INCOME",
                                      "userId": null
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody CreateCategoryRequest request) {
        
        // Always bind the category to the authenticated user.
        CreateCategoryRequest authenticatedRequest = new CreateCategoryRequest(
                request.name(), 
                request.type(), 
                user.getId()
        );
        
        CategoryResponse response = createCategoryUseCase.execute(authenticatedRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List categories available to the authenticated user")
    public ResponseEntity<List<CategoryResponse>> getCategories(@AuthenticationPrincipal User user) {
        List<CategoryResponse> categories = findCategoriesByUserIdUseCase.execute(user.getId());
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a custom category")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user,
            @RequestBody UpdateCategoryRequest request) {
        CategoryResponse response = updateCategoryUseCase.execute(id, user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a custom category")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        deleteCategoryUseCase.execute(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
