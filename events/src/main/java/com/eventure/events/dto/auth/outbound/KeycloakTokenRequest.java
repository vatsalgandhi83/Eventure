package com.eventure.events.dto.auth.outbound;

import lombok.Data;

@Data
public class KeycloakTokenRequest {
    private String client_id;
    private String username;
    private String password;
    private String grant_type;
} 