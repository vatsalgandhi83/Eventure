package com.eventure.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventure.events.Services.AuthService;
import com.eventure.events.dto.auth.inbound.SignupRequest;
import com.eventure.events.dto.auth.inbound.SignupResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest request) {
        System.out.println("Received signup request =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request);
        SignupResponse response = authService.signup(request);
        System.out.println("Signup response =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response);
        return ResponseEntity.ok(response);
    }
} 