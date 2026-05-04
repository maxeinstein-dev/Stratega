package br.com.maxsueleinstein.stratega.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("Deve criar uma categoria global válida")
    void shouldCreateValidGlobalCategory() {
        Category category = new Category(null, "Salário", CategoryType.INCOME, null);

        assertNotNull(category.getId());
        assertEquals("Salário", category.getName());
        assertEquals(CategoryType.INCOME, category.getType());
        assertTrue(category.isGlobal());
        assertNull(category.getUserId());
    }

    @Test
    @DisplayName("Deve criar uma categoria específica de usuário válida")
    void shouldCreateValidUserCategory() {
        UUID userId = UUID.randomUUID();
        Category category = new Category(null, "Lazer", CategoryType.EXPENSE, userId);

        assertEquals("Lazer", category.getName());
        assertEquals(CategoryType.EXPENSE, category.getType());
        assertFalse(category.isGlobal());
        assertEquals(userId, category.getUserId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome for nulo ou vazio")
    void shouldThrowExceptionWhenNameIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            new Category(null, "   ", CategoryType.EXPENSE, null)
        );
        assertEquals("O nome da categoria não pode estar em branco", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o tipo for nulo")
    void shouldThrowExceptionWhenTypeIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            new Category(null, "Alimentação", null, null)
        );
        assertEquals("O tipo da categoria é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar o nome da categoria com sucesso")
    void shouldUpdateNameSuccessfully() {
        Category category = new Category(null, "Alimentação", CategoryType.EXPENSE, null);
        category.updateName("Mercado");

        assertEquals("Mercado", category.getName());
    }
}
