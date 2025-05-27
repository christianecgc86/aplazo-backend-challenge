package com.aplazo.backend_challenge_demo.dto;

public record PurchaseResponseDTO(Long purchaseId) {
    @Override
    public String toString() {
        return "{ " +
                "purchaseId=" + purchaseId +
                " }";
    }
}
