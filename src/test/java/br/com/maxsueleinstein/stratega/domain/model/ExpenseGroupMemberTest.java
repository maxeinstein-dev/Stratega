package br.com.maxsueleinstein.stratega.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseGroupMemberTest {

    @Test
    @DisplayName("Deve criar um membro válido vinculado a um usuário")
    void shouldCreateValidMemberWithUser() {
        UUID userId = UUID.randomUUID();
        ExpenseGroupMember member = new ExpenseGroupMember(null, "John Doe", userId);

        assertNotNull(member.getId());
        assertEquals("John Doe", member.getName());
        assertEquals(userId, member.getUserId());
        assertTrue(member.isRegisteredUser());
    }

    @Test
    @DisplayName("Deve criar um membro virtual válido (sem usuário vinculado)")
    void shouldCreateValidVirtualMember() {
        ExpenseGroupMember member = new ExpenseGroupMember(null, "Jane Doe", null);

        assertNotNull(member.getId());
        assertEquals("Jane Doe", member.getName());
        assertNull(member.getUserId());
        assertFalse(member.isRegisteredUser());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar membro com nome em branco")
    void shouldThrowExceptionWhenNameIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            new ExpenseGroupMember(null, "  ", null)
        );

        assertEquals("O nome do membro não pode estar em branco", exception.getMessage());
    }
}
