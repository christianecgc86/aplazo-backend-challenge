package com.aplazo.backend_challenge_demo;

import com.aplazo.backend_challenge_demo.dto.PurchaseRequestDTO;
import com.aplazo.backend_challenge_demo.model.Client;
import com.aplazo.backend_challenge_demo.model.PaymentScheme;
import com.aplazo.backend_challenge_demo.model.Purchase;
import com.aplazo.backend_challenge_demo.repository.ClientRepository;
import com.aplazo.backend_challenge_demo.repository.PaymentSchemeRepository;
import com.aplazo.backend_challenge_demo.repository.PurchaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PurchaseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ClientRepository clientRepository;

	@MockitoBean
	private PurchaseRepository purchaseRepository;

	@MockitoBean
	private PaymentSchemeRepository paymentSchemeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testSavePurchase() throws Exception {
		PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
		purchaseRequestDTO.setClientId(1L);
		purchaseRequestDTO.setPurchaseAmount(10D);

		Client client = new Client();
		client.setClientId(1L);
		client.setFirstName("Test");
		client.setFirstName("Test");
		client.setAssignedCreditAmount(1000D);
		client.setAvailableCreditAmount(1000D);
		client.setBirthdate(new Date());

		Optional<Client> optionalClient = Optional.of(client);

		Purchase purchase = new Purchase();
		purchase.setPurchaseId(1L);
		purchase.setTotalAmount(11.6);

		Mockito.when(clientRepository.findById(Mockito.any(Long.class)))
				.thenReturn(optionalClient);

		PaymentScheme paymentScheme1 = new PaymentScheme();
		paymentScheme1.setPaymentSchemeId(1L);
		paymentScheme1.setName("Scheme 1");
		paymentScheme1.setNumberOfPayments(5);
		paymentScheme1.setFrequency(14);
		paymentScheme1.setInterestRate(0.13);

		PaymentScheme paymentScheme2 = new PaymentScheme();
		paymentScheme2.setPaymentSchemeId(2L);
		paymentScheme2.setName("Scheme 2");
		paymentScheme2.setNumberOfPayments(5);
		paymentScheme2.setFrequency(14);
		paymentScheme2.setInterestRate(0.13);

		Mockito.when(paymentSchemeRepository.findById(1L)).thenReturn(Optional.of(paymentScheme1));
		Mockito.when(paymentSchemeRepository.findById(2L)).thenReturn(Optional.of(paymentScheme2));

		Mockito.when(clientRepository.saveAndFlush(Mockito.any(Client.class)))
				.thenReturn(client);

		Mockito.when(purchaseRepository.save(Mockito.any(Purchase.class)))
				.thenReturn(purchase);

		mockMvc.perform(post("/aplazo/purchases")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(purchaseRequestDTO)))
				.andExpect(status().isOk());
	}

	@Test
	void testSavePurchaseNoClientId() throws Exception {
		PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
		purchaseRequestDTO.setPurchaseAmount(10D);

		mockMvc.perform(post("/aplazo/purchases")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(purchaseRequestDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Purchase could not be saved: Client Id cannot be null | "));

	}

	@Test
	void testSavePurchaseClientIdNotFoundInDB() throws Exception {
		PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
		purchaseRequestDTO.setClientId(1L);
		purchaseRequestDTO.setPurchaseAmount(10D);

		Mockito.when(clientRepository.findById(Mockito.any(Long.class)))
				.thenReturn(Optional.empty());

		mockMvc.perform(post("/aplazo/purchases")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(purchaseRequestDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Purchase could not be saved: Client Id was not found"));
	}

	@Test
	void testSavePurchaseNoFunds() throws Exception {
		PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
		purchaseRequestDTO.setClientId(1L);
		purchaseRequestDTO.setPurchaseAmount(1000D);

		Client client = new Client();
		client.setClientId(1L);
		client.setFirstName("Test");
		client.setFirstName("Test");
		client.setAssignedCreditAmount(1000D);
		client.setAvailableCreditAmount(1D);
		client.setBirthdate(new Date());

		Optional<Client> optionalClient = Optional.of(client);

		Mockito.when(clientRepository.findById(Mockito.any(Long.class)))
				.thenReturn(optionalClient);

		PaymentScheme paymentScheme1 = new PaymentScheme();
		paymentScheme1.setPaymentSchemeId(1L);
		paymentScheme1.setName("Scheme 1");
		paymentScheme1.setNumberOfPayments(5);
		paymentScheme1.setFrequency(14);
		paymentScheme1.setInterestRate(0.13);

		PaymentScheme paymentScheme2 = new PaymentScheme();
		paymentScheme2.setPaymentSchemeId(2L);
		paymentScheme2.setName("Scheme 2");
		paymentScheme2.setNumberOfPayments(5);
		paymentScheme2.setFrequency(14);
		paymentScheme2.setInterestRate(0.13);

		Mockito.when(paymentSchemeRepository.findById(1L)).thenReturn(Optional.of(paymentScheme1));
		Mockito.when(paymentSchemeRepository.findById(2L)).thenReturn(Optional.of(paymentScheme2));

		mockMvc.perform(post("/aplazo/purchases")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(purchaseRequestDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Purchase could not be saved: Insufficient funds in the client account"));
	}

}
