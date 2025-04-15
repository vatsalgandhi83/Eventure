package com.eventure.events.dto.auth.outbound;

import lombok.Data;

@Data
public class KeycloakTokenResponse {
    private String access_token;
    private String refresh_token;
    private int expires_in;
    private String token_type;
    private String scope;
} 