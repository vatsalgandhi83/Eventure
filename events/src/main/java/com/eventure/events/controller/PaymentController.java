package com.eventure.events.controller;

import com.eventure.events.Services.PaymentService;
import com.eventure.events.dto.PaymentRequest;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class PaymentController {

    @Autowired
    private PaymentService payPalService;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody @Valid PaymentRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || request.getAmount() == null || request.getAmount().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid or missing amount");
        }
        try {
            String approvalUrl = payPalService.createPayment(request.getAmount());
            return ResponseEntity.ok(approvalUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Payment creation failed: " + e.getMessage());
        }
    }
}

