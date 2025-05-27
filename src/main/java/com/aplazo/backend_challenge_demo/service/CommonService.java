package com.aplazo.backend_challenge_demo.service;

import com.aplazo.backend_challenge_demo.dto.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface CommonService {
    ResponseEntity<ErrorDTO> getValidationError(BindingResult result);
}
