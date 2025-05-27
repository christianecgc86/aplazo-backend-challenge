package com.aplazo.backend_challenge_demo.service;

import com.aplazo.backend_challenge_demo.dto.PurchaseRequestDTO;
import com.aplazo.backend_challenge_demo.dto.PurchaseResponseDTO;

public interface PurchaseService extends CommonService {
    PurchaseResponseDTO savePurchase(PurchaseRequestDTO purchaseRequestDTO) throws Exception;
}
