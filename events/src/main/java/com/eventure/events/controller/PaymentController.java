package com.eventure.events.controller;

import com.eventure.events.Services.PaymentService;
import com.eventure.events.dto.PaymentRequest;
import com.eventure.events.dto.PaymentResponse;

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
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody @Valid PaymentRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request parameters");
            return ResponseEntity.badRequest()
                .body(new PaymentResponse("error", errorMessage, "VALIDATION_ERROR", null));
        }

        PaymentResponse response = payPalService.createPayment(request.getAmount());
        return ResponseEntity.ok(response);
    }
}

