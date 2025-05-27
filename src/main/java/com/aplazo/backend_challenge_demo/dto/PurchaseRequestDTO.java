package com.aplazo.backend_challenge_demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PurchaseRequestDTO {

    @NotNull(message = "Client Id cannot be null")
    @Min(value = 1, message = "Client Id must be at least 1")
    private Long clientId;

    @NotNull(message = "Purchase Amount cannot be null")
    @DecimalMin(value = "1.0", inclusive = true, message = "Purchase Amount must be at least 1.0")
    private Double purchaseAmount;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    @Override
    public String toString() {
        return "{ " +
                "clientId=" + clientId +
                ", purchaseAmount=" + purchaseAmount +
                " }";
    }

}
