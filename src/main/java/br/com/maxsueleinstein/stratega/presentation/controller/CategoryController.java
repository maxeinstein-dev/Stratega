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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Endpoints para gerenciamento de categorias de transações")
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
    @Operation(summary = "Criar nova categoria customizada")
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody CreateCategoryRequest request) {
        
        // Garante que a categoria seja criada para o usuário logado
        CreateCategoryRequest authenticatedRequest = new CreateCategoryRequest(
                request.name(), 
                request.type(), 
                user.getId()
        );
        
        CategoryResponse response = createCategoryUseCase.execute(authenticatedRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar categorias acessíveis ao usuário (Globais + Customizadas)")
    public ResponseEntity<List<CategoryResponse>> getCategories(@AuthenticationPrincipal User user) {
        List<CategoryResponse> categories = findCategoriesByUserIdUseCase.execute(user.getId());
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar nome ou tipo de uma categoria customizada")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user,
            @RequestBody UpdateCategoryRequest request) {
        CategoryResponse response = updateCategoryUseCase.execute(id, user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma categoria customizada (Bloqueado se estiver em uso)")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        deleteCategoryUseCase.execute(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
