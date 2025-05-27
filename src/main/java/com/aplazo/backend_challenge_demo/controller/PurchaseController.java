package com.aplazo.backend_challenge_demo.controller;

import com.aplazo.backend_challenge_demo.dto.ErrorDTO;
import com.aplazo.backend_challenge_demo.dto.PurchaseRequestDTO;
import com.aplazo.backend_challenge_demo.dto.PurchaseResponseDTO;
import com.aplazo.backend_challenge_demo.service.PurchaseService;
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
@RequestMapping("/aplazo/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @PostMapping
    public ResponseEntity<?> savePurchase(@Valid @RequestBody PurchaseRequestDTO purchaseRequestDTO, BindingResult result) {
        try {
            logger.info("Saving Purchase Request: {}", purchaseRequestDTO);
            if (result.hasErrors()) {
                return purchaseService.getValidationError(result);
            }
            PurchaseResponseDTO purchaseResponseDTO = purchaseService.savePurchase(purchaseRequestDTO);
            logger.info("Saving Purchase Response: {}", purchaseResponseDTO);
            return ResponseEntity.ok(purchaseResponseDTO);
        } catch (Exception e) {
            ErrorDTO errorDTO = new ErrorDTO(
                    400,
                    "Bad Request",
                    "Purchase could not be saved: " + e.getMessage()
            );
            logger.error("Saving Purchase Response: {}", errorDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

}
