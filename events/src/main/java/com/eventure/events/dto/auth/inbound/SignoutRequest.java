package com.eventure.events.dto.auth.inbound;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
public class SignoutRequest {
    private String refreshToken;
} 