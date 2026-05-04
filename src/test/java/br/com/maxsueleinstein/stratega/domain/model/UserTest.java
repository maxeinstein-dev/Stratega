package br.com.maxsueleinstein.stratega.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Deve criar um usuário válido com ID gerado automaticamente e senha criptografada")
    void shouldCreateValidUserWithGeneratedId() {
        User user = new User(null, "John Doe", "john@example.com", "encoded-password");

        assertNotNull(user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("encoded-password", user.getPassword());
    }

    @Test
    @DisplayName("Deve criar um usuário válido com ID fornecido")
    void shouldCreateValidUserWithProvidedId() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Jane Doe", "jane@example.com", "secret");

        assertEquals(id, user.getId());
        assertEquals("secret", user.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome for nulo ou vazio")
    void shouldThrowExceptionWhenNameIsInvalid() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> 
            new User(null, null, "john@example.com", "pass")
        );
        assertEquals("O nome do usuário não pode estar em branco", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> 
            new User(null, "   ", "john@example.com", "pass")
        );
        assertEquals("O nome do usuário não pode estar em branco", exception2.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o email for nulo ou inválido")
    void shouldThrowExceptionWhenEmailIsInvalid() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> 
            new User(null, "John Doe", null, "pass")
        );
        assertEquals("O email informado é inválido", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> 
            new User(null, "John Doe", "invalid-email", "pass")
        );
        assertEquals("O email informado é inválido", exception2.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha for nula ou muito curta")
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> 
            new User(null, "John Doe", "john@example.com", null)
        );
        assertEquals("A senha não pode estar em branco e deve ter pelo menos 6 caracteres", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> 
            new User(null, "John Doe", "john@example.com", "12345")
        );
        assertEquals("A senha não pode estar em branco e deve ter pelo menos 6 caracteres", exception2.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar perfil com sucesso")
    void shouldUpdateProfileSuccessfully() {
        User user = new User(null, "John", "john@example.com", "secret123");
        user.updateProfile("John Updated", "new@example.com");

        assertEquals("John Updated", user.getName());
        assertEquals("new@example.com", user.getEmail());
    }
}
