package com.eventure.events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/paypal")
public class PaypalCallbackController {

    @GetMapping("/success")
    public ResponseEntity<Map<String, Object>> paymentSuccess(@RequestParam(required = false) String token, @RequestParam(required = false) String PayerID) {
        Map<String, Object> response = new HashMap<>();
        if (token == null || PayerID == null) {
            response.put("status", "error");
            response.put("message", "Missing required parameters");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("status", "success");
        response.put("token", token);
        response.put("payerId", PayerID);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cancel")
    public ResponseEntity<Map<String, Object>> paymentCancelled() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "cancelled");
        response.put("message", "Payment Cancelled!");
        return ResponseEntity.ok(response);
    }
}
