package com.eventure.events.dto.auth.inbound;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class SigninRequest {
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
} 