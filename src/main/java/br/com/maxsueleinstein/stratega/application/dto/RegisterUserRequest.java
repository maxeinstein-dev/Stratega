package br.com.maxsueleinstein.stratega.application.dto;

public record RegisterUserRequest(
        String name,
        String email,
        String password
) {}
