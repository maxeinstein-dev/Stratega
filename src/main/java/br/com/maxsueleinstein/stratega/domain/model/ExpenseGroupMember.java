package br.com.maxsueleinstein.stratega.domain.model;

import java.util.Objects;
import java.util.UUID;

public class ExpenseGroupMember {

    private final UUID id;
    private final String name;
    private final UUID userId;

    public ExpenseGroupMember(UUID id, String name, UUID userId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do membro não pode estar em branco");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isRegisteredUser() {
        return userId != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseGroupMember that = (ExpenseGroupMember) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
