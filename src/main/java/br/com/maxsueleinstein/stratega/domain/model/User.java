package br.com.maxsueleinstein.stratega.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a User.
 * 
 * Code is in English, validations and business exceptions in pt-BR.
 */
public class User {

    private final UUID id;
    private String name;
    private String email;
    private String password;

    public User(UUID id, String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateProfile(String name, String email) {
        validateName(name);
        validateEmail(email);
        this.name = name;
        this.email = email;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário não pode estar em branco");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("O email informado é inválido");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().length() < 6) {
            throw new IllegalArgumentException("A senha não pode estar em branco e deve ter pelo menos 6 caracteres");
        }
    }

    public String getPassword() {
        return password;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
