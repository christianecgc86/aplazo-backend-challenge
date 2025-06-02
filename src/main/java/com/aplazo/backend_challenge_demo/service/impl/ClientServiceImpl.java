package com.aplazo.backend_challenge_demo.service.impl;

import com.aplazo.backend_challenge_demo.dto.ClientRequestDTO;
import com.aplazo.backend_challenge_demo.dto.ClientResponseDTO;
import com.aplazo.backend_challenge_demo.dto.ErrorDTO;
import com.aplazo.backend_challenge_demo.model.Client;
import com.aplazo.backend_challenge_demo.repository.ClientRepository;
import com.aplazo.backend_challenge_demo.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Override
    public ClientResponseDTO saveClient(ClientRequestDTO clientRequestDTO) throws Exception {
        Client client = createEntity(clientRequestDTO);

        validateIfClientExists(client);

        int age = calculateAge(clientRequestDTO.getBirthdate());
        validateAgeAndAssignCreditAmount(age, client);

        Client savedClient = clientRepository.save(client);
        return createResponse(savedClient);
    }

    @Override
    public ResponseEntity<ErrorDTO> getValidationError(BindingResult result) {
        StringBuilder sb = new StringBuilder("Client could not be saved: ");
        for (FieldError fieldError : result.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage());
            sb.append(" | ");
        }
        ErrorDTO errorDTO = new ErrorDTO(
                400,
                "Bad Request",
                sb.toString()
        );
        logger.error("Saving Client Response: {}", errorDTO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    private ClientResponseDTO createResponse(Client client) {
        return new ClientResponseDTO(client.getClientId(), client.getAssignedCreditAmount());
    }

    private Client createEntity(ClientRequestDTO clientRequestDTO) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Client client = new Client();
        client.setFirstName(clientRequestDTO.getFirstName());
        client.setLastName(clientRequestDTO.getLastName());
        client.setBirthdate(sdf.parse(clientRequestDTO.getBirthdate()));
        return client;
    }

    private int calculateAge(String strBirthdate) {
        LocalDate birthdate = LocalDate.parse(strBirthdate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthdate, currentDate).getYears();
    }

    private void validateAgeAndAssignCreditAmount(int age, Client client) throws Exception {
        if (age<18 || age>65)
            throw new Exception("Age must be between 18 and 65");

        if (age<=25) {
            client.setAssignedCreditAmount(3000D);
        } else if (age<=30) {
            client.setAssignedCreditAmount(5000D);
        } else {
            client.setAssignedCreditAmount(8000D);
        }

        client.setAvailableCreditAmount(client.getAssignedCreditAmount());
    }

    private void validateIfClientExists(Client client) throws Exception {
        List<Client> list = clientRepository.findByFirstNameAndLastNameAndBirthdate(client.getFirstName(), client.getLastName(), client.getBirthdate());
        if (list!=null && !list.isEmpty()) {
            throw new Exception("Client with the same data was found in DB");
        }
    }

}
