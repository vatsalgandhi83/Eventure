package com.eventure.events.dto;

import jakarta.validation.constraints.NotBlank;

public class PaymentRequest {
    @NotBlank(message = "Amount is required")
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
} 