package com.aplazo.backend_challenge_demo.dto;

public record ClientResponseDTO(Long clientId, Double assignedCreditAmount) {
    @Override
    public String toString() {
        return "{ " +
                "clientId=" + clientId +
                ", assignedCreditAmount=" + assignedCreditAmount +
                " }";
    }
}
