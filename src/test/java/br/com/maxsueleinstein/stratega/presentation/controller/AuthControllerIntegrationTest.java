package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.AuthenticateUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.RegisterUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.TokenResponse;
import br.com.maxsueleinstein.stratega.application.dto.UserResponse;
import br.com.maxsueleinstein.stratega.application.usecase.AuthenticateUserUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.RegisterUserUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private AuthenticateUserUseCase authenticateUserUseCase;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    void shouldRegisterUserAndReturnCreated() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest("John Doe", "john@example.com", "password");
        UserResponse response = new UserResponse(UUID.randomUUID(), "John Doe", "john@example.com");

        when(registerUserUseCase.execute(any(RegisterUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldAuthenticateUserAndReturnToken() throws Exception {
        AuthenticateUserRequest request = new AuthenticateUserRequest("john@example.com", "password");
        TokenResponse response = new TokenResponse("fake-jwt-token");

        when(authenticateUserUseCase.execute(any(AuthenticateUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-token"));
    }
}
