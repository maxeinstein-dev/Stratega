package br.com.maxsueleinstein.stratega.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(@NotNull String name, @Email @NotNull String email, @NotNull String password) {
}
