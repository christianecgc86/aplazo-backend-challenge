package com.aplazo.backend_challenge_demo.service;

import com.aplazo.backend_challenge_demo.dto.ClientRequestDTO;
import com.aplazo.backend_challenge_demo.dto.ClientResponseDTO;

public interface ClientService extends CommonService {
    ClientResponseDTO saveClient(ClientRequestDTO clientRequestDTO) throws Exception;
}
