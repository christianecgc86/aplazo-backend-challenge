package com.aplazo.backend_challenge_demo;

import com.aplazo.backend_challenge_demo.model.Client;
import com.aplazo.backend_challenge_demo.repository.ClientRepository;
import com.aplazo.backend_challenge_demo.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:payment_schemes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class FlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @BeforeEach
    void cleanDatabase() {
        clientRepository.deleteAll();
        purchaseRepository.deleteAll();
    }

    @Test
    void testFlow() throws Exception {
        String requestJson = """
            {
              "firstName": "Test",
              "lastName": "Test",
              "birthdate": "1986/11/03"
            }
        """;

        mockMvc.perform(post("/aplazo/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").isNumber())
                .andExpect(jsonPath("$.assignedCreditAmount").value(8000D))
                .andReturn();

        List<Client> clients = clientRepository.findAll();
        assertEquals(1, clients.size());
        Client client = clients.getFirst();
        assertEquals("Test", client.getFirstName());
        assertEquals("Test", client.getLastName());
        assertEquals(8000D, client.getAssignedCreditAmount());


        String purchaseRequestJson = String.format("""
            {
              "clientId": %d,
              "purchaseAmount": 500.0
            }
        """, client.getClientId());

        mockMvc.perform(post("/aplazo/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(purchaseRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseId").exists());

        Client clientSaved = clientRepository.findById(client.getClientId()).orElseThrow();
        assertEquals(1, clientSaved.getPurchaseList().size());

    }

}
