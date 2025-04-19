package com.eventure.events.dto.auth.outbound;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakIntrospectTokenRequest {
    private String token;
    private String clientId;
    private String clientSecret;
}
