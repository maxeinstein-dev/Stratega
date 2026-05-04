package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.CreateWalletRequest;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
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

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    void shouldCreateWalletAndReturnCreatedStatus() throws Exception {
        CreateWalletRequest request = new CreateWalletRequest("Carteira Viagem", BigDecimal.valueOf(100), UUID.randomUUID());

        mockMvc.perform(post("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Carteira Viagem"))
                .andExpect(jsonPath("$.balance").value(100));
    }
}
