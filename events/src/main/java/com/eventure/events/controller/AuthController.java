package com.eventure.events.controller;

import com.eventure.events.dto.auth.inbound.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventure.events.Services.AuthService;

import jakarta.validation.Valid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

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

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody @Valid SigninRequest request) {
        System.out.println("Received signin request =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request);
        SigninResponse response = authService.signin(request);

        if (!"success".equals(response.getStatus())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        // build an HttpOnly cookie for the refresh token
        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofSeconds(response.getRefreshExpiresIn()))
                .sameSite("Lax")
                .build();

        SigninResponse jsonBody = SigninResponse.builder()
                .message(response.getMessage())
                .status(response.getStatus())
                .accessToken(response.getAccessToken())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(jsonBody);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Received signout request =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + request.getRequestURI());

        // Fetch refresh token from cookies
        String refreshToken = authService.fetchRefreshToken(request);

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token not found.");
        }

        // Call logout
        authService.signout(SignoutRequest.builder().refreshToken(refreshToken).build());

        // Clear cookie after logout
        authService.clearRefreshTokenCookie(response);

        System.out.println("Signout response =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return ResponseEntity.ok("Signout successful");
    }

} 