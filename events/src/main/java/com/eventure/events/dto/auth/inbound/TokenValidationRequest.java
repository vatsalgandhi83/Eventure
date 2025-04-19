package com.eventure.events.dto.auth.inbound;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenValidationRequest {
    private String token;
}
