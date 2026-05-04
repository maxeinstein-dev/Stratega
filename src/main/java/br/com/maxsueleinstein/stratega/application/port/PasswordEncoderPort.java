package br.com.maxsueleinstein.stratega.application.port;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
