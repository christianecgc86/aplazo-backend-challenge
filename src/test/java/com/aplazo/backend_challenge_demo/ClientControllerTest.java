package com.aplazo.backend_challenge_demo;

import com.aplazo.backend_challenge_demo.dto.ClientRequestDTO;
import com.aplazo.backend_challenge_demo.model.Client;
import com.aplazo.backend_challenge_demo.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ClientRepository clientRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testSaveClient() throws Exception {
		ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
		clientRequestDTO.setFirstName("Test");
		clientRequestDTO.setLastName("Test");
		clientRequestDTO.setBirthdate("1986/11/03");

		Client client = new Client();
		client.setClientId(1L);
		client.setAssignedCreditAmount(8000D);

		Optional<Client> optionalClient = Optional.of(client);

		Mockito.when(clientRepository.save(Mockito.any(Client.class)))
				.thenReturn(client);

		Mockito.when(clientRepository.findById(Mockito.any(Long.class)))
				.thenReturn(optionalClient);

		mockMvc.perform(post("/aplazo/clients")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(clientRequestDTO)))
				.andExpect(status().isOk());

        assertTrue(clientRepository.findById(1L).isPresent());
	}

	@Test
	void testSaveClientNoFirstName() throws Exception {
		ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
		clientRequestDTO.setLastName("Test");
		clientRequestDTO.setBirthdate("1986/11/03");

		mockMvc.perform(post("/aplazo/clients")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(clientRequestDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Client could not be saved: First name is required | "));
	}

	@Test
	void testSaveClientInvalidAge() throws Exception {
		ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
		clientRequestDTO.setFirstName("Test");
		clientRequestDTO.setLastName("Test");
		clientRequestDTO.setBirthdate("2020/11/03");

		mockMvc.perform(post("/aplazo/clients")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(clientRequestDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Client could not be saved: Age must be between 18 and 65"));
	}

	@Test
	void testSaveClientInvalidBirthdate() throws Exception {
		ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
		clientRequestDTO.setFirstName("Test");
		clientRequestDTO.setLastName("Test");
		clientRequestDTO.setBirthdate("2020");

		mockMvc.perform(post("/aplazo/clients")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(clientRequestDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Client could not be saved: Date format should be yyyy/MM/dd | "));
	}

}
