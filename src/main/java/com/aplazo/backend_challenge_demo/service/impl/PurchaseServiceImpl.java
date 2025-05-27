package com.aplazo.backend_challenge_demo.service.impl;

import com.aplazo.backend_challenge_demo.dto.ErrorDTO;
import com.aplazo.backend_challenge_demo.dto.PurchaseRequestDTO;
import com.aplazo.backend_challenge_demo.dto.PurchaseResponseDTO;
import com.aplazo.backend_challenge_demo.model.Client;
import com.aplazo.backend_challenge_demo.model.PaymentScheme;
import com.aplazo.backend_challenge_demo.model.Purchase;
import com.aplazo.backend_challenge_demo.repository.ClientRepository;
import com.aplazo.backend_challenge_demo.repository.PaymentSchemeRepository;
import com.aplazo.backend_challenge_demo.repository.PurchaseRepository;
import com.aplazo.backend_challenge_demo.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PaymentSchemeRepository paymentSchemeRepository;

    private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    @Override
    public PurchaseResponseDTO savePurchase(PurchaseRequestDTO purchaseRequestDTO) throws Exception {
        Client client = this.getClient(purchaseRequestDTO.getClientId());

        PaymentScheme paymentScheme = this.getPaymentScheme(client);

        Double purchaseAmount = purchaseRequestDTO.getPurchaseAmount();
        Purchase purchase = createEntity(client, paymentScheme, purchaseAmount);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        this.updateClientAvailableAmount(client, savedPurchase.getTotalAmount());
        return this.createResponse(savedPurchase);
    }

    @Override
    public ResponseEntity<ErrorDTO> getValidationError(BindingResult result) {
        StringBuilder sb = new StringBuilder("Purchase could not be saved: ");
        for (FieldError fieldError : result.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage());
            sb.append(" | ");
        }
        ErrorDTO errorDTO = new ErrorDTO(
                400,
                "Bad Request",
                sb.toString()
        );
        logger.error("Saving Purchase Response: {}", errorDTO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    private Client getClient(Long clientId) throws Exception {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isEmpty())
            throw new Exception("Client Id was not found");
        return optionalClient.get();
    }

    private PaymentScheme getPaymentScheme(Client client) throws Exception {
        Optional<PaymentScheme> optionalPaymentScheme = paymentSchemeRepository.findById(2L);
        if (client.getFirstName().toUpperCase().startsWith("C")
                || client.getFirstName().toUpperCase().startsWith("H")
                || client.getFirstName().toUpperCase().startsWith("L"))
            optionalPaymentScheme = paymentSchemeRepository.findById(1L);

        if (optionalPaymentScheme.isEmpty())
            throw new Exception("Payment Scheme was not found");

        return optionalPaymentScheme.get();
    }

    private Purchase createEntity(Client client, PaymentScheme paymentScheme, Double purchaseAmount) throws Exception {
        Double commissionAmount = purchaseAmount*paymentScheme.getInterestRate();
        Double totalAmount = commissionAmount + purchaseAmount;
        Double installmentAmount = totalAmount / paymentScheme.getNumberOfPayments();

        if (Double.compare(totalAmount,client.getAvailableCreditAmount())>0)
            throw new Exception("Insufficient funds in the client account");

        Date date = new Date();
        List<Date> dateList = this.getDueDates(paymentScheme.getNumberOfPayments(), paymentScheme.getFrequency());

        Purchase purchase = new Purchase();
        purchase.setPurchaseDate(date);
        purchase.setCommissionAmount(commissionAmount);
        purchase.setTotalAmount(totalAmount);
        purchase.setInstallmentAmount(installmentAmount);
        purchase.setDueDates(dateList);
        purchase.setClient(client);

        return purchase;
    }

    private List<Date> getDueDates(int numberOfPayments, int frequency) {
        List<Date> dateList = new ArrayList<>();
        LocalDate localDate = LocalDate.now();

        for (int i = 1; i <= numberOfPayments; i++) {
            int days = i*frequency;
            LocalDate localFutureDate = localDate.plusDays(days);
            Date date = Date.from(localFutureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateList.add(date);
        }

        return dateList;
    }

    private PurchaseResponseDTO createResponse(Purchase purchase) {
        return new PurchaseResponseDTO(purchase.getPurchaseId());
    }

    private void updateClientAvailableAmount(Client client, double purchaseAmount) {
        double availableCreditAmount = client.getAvailableCreditAmount() - purchaseAmount;
        client.setAvailableCreditAmount(availableCreditAmount);
        clientRepository.saveAndFlush(client);
    }

}
