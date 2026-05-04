package br.com.maxsueleinstein.stratega.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a Category (global or specific to a user).
 */
public class Category {

    private final UUID id;
    private String name;
    private final CategoryType type;
    private final UUID userId; // null if global category

    public Category(UUID id, String name, CategoryType type, UUID userId) {
        validateName(name);
        if (type == null) {
            throw new IllegalArgumentException("O tipo da categoria é obrigatório");
        }
        
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.userId = userId;
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode estar em branco");
        }
    }

    public boolean isGlobal() {
        return this.userId == null;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
