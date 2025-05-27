package com.aplazo.backend_challenge_demo.controller;

import com.aplazo.backend_challenge_demo.dto.ClientRequestDTO;
import com.aplazo.backend_challenge_demo.dto.ClientResponseDTO;
import com.aplazo.backend_challenge_demo.dto.ErrorDTO;
import com.aplazo.backend_challenge_demo.service.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aplazo/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @PostMapping
    public ResponseEntity<?> saveClient(@Valid @RequestBody ClientRequestDTO clientRequestDTO, BindingResult result) {
        try {
            logger.info("Saving Client Request: {}", clientRequestDTO);
            if (result.hasErrors()) {
                return clientService.getValidationError(result);
            }
            ClientResponseDTO clientResponseDTO = clientService.saveClient(clientRequestDTO);
            logger.info("Saving Client Response: {}", clientResponseDTO);
            return ResponseEntity.ok(clientResponseDTO);
        } catch (Exception e) {
            ErrorDTO errorDTO = new ErrorDTO(
                    400,
                    "Bad Request",
                    "Client could not be saved: " + e.getMessage()
            );
            logger.error("Saving Client Response: {}", errorDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

}
