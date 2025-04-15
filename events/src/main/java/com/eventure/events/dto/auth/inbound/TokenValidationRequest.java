package com.eventure.events.dto.auth.inbound;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String token;
}
