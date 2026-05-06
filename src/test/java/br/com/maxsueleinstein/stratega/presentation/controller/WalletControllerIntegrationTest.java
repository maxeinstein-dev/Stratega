package br.com.maxsueleinstein.stratega.presentation.controller;
 
import br.com.maxsueleinstein.stratega.application.dto.CreateWalletRequest;
import br.com.maxsueleinstein.stratega.domain.model.Currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
public class WalletControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private br.com.maxsueleinstein.stratega.domain.repository.UserRepository userRepository;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private br.com.maxsueleinstein.stratega.application.port.JwtTokenProviderPort jwtTokenProviderPort;

    private ObjectMapper objectMapper = new ObjectMapper();
    private UUID userId;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity())
                .build();

        userId = UUID.randomUUID();
        br.com.maxsueleinstein.stratega.domain.model.User user = new br.com.maxsueleinstein.stratega.domain.model.User(
            userId, "User", "user@example.com", "password"
        );
        userRepository.save(user);

        org.mockito.Mockito.when(jwtTokenProviderPort.validateToken(org.mockito.ArgumentMatchers.anyString())).thenReturn(true);
        org.mockito.Mockito.when(jwtTokenProviderPort.getUserIdFromToken(org.mockito.ArgumentMatchers.anyString())).thenReturn(userId.toString());
    }

    @Test
    void shouldCreateWalletAndReturnCreatedStatus() throws Exception {
        CreateWalletRequest request = new CreateWalletRequest("Carteira Viagem", BigDecimal.valueOf(100), UUID.randomUUID(), Currency.BRL, null);

        mockMvc.perform(post("/api/wallets")
                .header("Authorization", "Bearer dummy-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Carteira Viagem"))
                .andExpect(jsonPath("$.balance").value(100));
    }
}
