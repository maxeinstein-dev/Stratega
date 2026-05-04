package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.AddGroupExpenseRequest;
import br.com.maxsueleinstein.stratega.application.dto.CreateGroupRequest;
import br.com.maxsueleinstein.stratega.application.port.JwtTokenProviderPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class GroupControllerIntegrationTest {

        private MockMvc mockMvc;

        @Autowired
        private WebApplicationContext context;

        @MockitoBean
        private JwtTokenProviderPort jwtTokenProviderPort;

        private ObjectMapper objectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        @BeforeEach
        void setUp() {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                                .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity())
                                .build();
                // Mock token validation to allow access
                when(jwtTokenProviderPort.validateToken(anyString())).thenReturn(true);
        }

        @Test
        @DisplayName("Deve criar um grupo, adicionar despesa e consultar balanços")
        void shouldCreateGroupAddExpenseAndGetBalances() throws Exception {
                UUID ownerId = UUID.randomUUID();
                when(jwtTokenProviderPort.getUserIdFromToken(anyString())).thenReturn(ownerId.toString());
                
                CreateGroupRequest createRequest = new CreateGroupRequest("Viagem", ownerId, List.of("Alice", "Bob"));

                // 1. Criar Grupo
                String createResponseJson = mockMvc.perform(post("/api/groups")
                                .header("Authorization", "Bearer dummy-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Viagem"))
                                .andReturn().getResponse().getContentAsString();

                br.com.maxsueleinstein.stratega.application.dto.GroupResponse group = objectMapper.readValue(
                                createResponseJson,
                                br.com.maxsueleinstein.stratega.application.dto.GroupResponse.class);
                UUID groupId = group.id();
                UUID aliceId = group.members().get(0).name().equals("Alice") ? group.members().get(0).id()
                                : group.members().get(1).id();
                UUID bobId = group.members().get(0).name().equals("Bob") ? group.members().get(0).id()
                                : group.members().get(1).id();

                // 2. Adicionar Despesa (Alice pagou 100, dividido igualmente)
                AddGroupExpenseRequest expenseRequest = new AddGroupExpenseRequest(
                                groupId, ownerId, "Jantar", new BigDecimal("100.00"), aliceId, java.time.LocalDateTime.now(), "UNIFORM", Map.of());

                mockMvc.perform(post("/api/groups/" + groupId + "/expenses")
                                .header("Authorization", "Bearer dummy-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(expenseRequest)))
                                .andExpect(status().isOk());

                // 3. Consultar Balanços
                mockMvc.perform(get("/api/groups/" + groupId + "/balances")
                                .header("Authorization", "Bearer dummy-token"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.memberBalances." + aliceId).value(50.0))
                                .andExpect(jsonPath("$.memberBalances." + bobId).value(-50.0))
                                .andExpect(jsonPath("$.suggestedTransfers[0].amount").value(50.0))
                                .andExpect(jsonPath("$.suggestedTransfers[0].from.name").value("Bob"))
                                .andExpect(jsonPath("$.suggestedTransfers[0].to.name").value("Alice"));
        }

        @Test
        @DisplayName("Deve criar um grupo, adicionar despesa com divisão exata e consultar balanços")
        void shouldCreateGroupWithExactSplit() throws Exception {
                UUID ownerId = UUID.randomUUID();
                when(jwtTokenProviderPort.getUserIdFromToken(anyString())).thenReturn(ownerId.toString());

                CreateGroupRequest createRequest = new CreateGroupRequest("Aluguel", ownerId, List.of("Alice", "Bob"));

                String createResponseJson = mockMvc.perform(post("/api/groups")
                                .header("Authorization", "Bearer dummy-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                br.com.maxsueleinstein.stratega.application.dto.GroupResponse group = objectMapper.readValue(
                                createResponseJson,
                                br.com.maxsueleinstein.stratega.application.dto.GroupResponse.class);
                UUID groupId = group.id();
                UUID aliceId = group.members().get(0).name().equals("Alice") ? group.members().get(0).id()
                                : group.members().get(1).id();
                UUID bobId = group.members().get(0).name().equals("Bob") ? group.members().get(0).id()
                                : group.members().get(1).id();

                // 2. Adicionar Despesa Exata (Alice pagou 150, Alice deve 100, Bob deve 50)
                AddGroupExpenseRequest expenseRequest = new AddGroupExpenseRequest(
                                groupId, ownerId, "Aluguel Maio", new BigDecimal("150.00"), aliceId, java.time.LocalDateTime.now(), "EXACT",
                                Map.of(aliceId, "100.00", bobId, "50.00"));

                mockMvc.perform(post("/api/groups/" + groupId + "/expenses")
                                .header("Authorization", "Bearer dummy-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(expenseRequest)))
                                .andExpect(status().isOk());

                // 3. Consultar Balanços
                // Alice pagou 150, deve 100 -> Saldo +50
                // Bob pagou 0, deve 50 -> Saldo -50
                mockMvc.perform(get("/api/groups/" + groupId + "/balances")
                                .header("Authorization", "Bearer dummy-token"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.memberBalances." + aliceId).value(50.0))
                                .andExpect(jsonPath("$.memberBalances." + bobId).value(-50.0));
        }
}
