package com.eventure.events.dto.auth.outbound;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakGetTokenRequest {
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String refreshToken;
}
