package com.eventure.events.dto.auth.inbound;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
} 