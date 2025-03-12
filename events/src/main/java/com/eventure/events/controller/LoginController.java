package com.eventure.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventure.events.Services.UserService;
import com.eventure.events.dto.Login;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid Login loginRequest) {
		boolean isAuthenticated = userService.authenticateUser(loginRequest);

		if (isAuthenticated) {
			return ResponseEntity.ok("Login successful");
		} else {
			return ResponseEntity.status(401).body("Invalid email or password");
		}
	}

}
