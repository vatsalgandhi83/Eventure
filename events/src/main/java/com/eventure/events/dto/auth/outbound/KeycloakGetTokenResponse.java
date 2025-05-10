package com.eventure.events.dto.auth.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeycloakGetTokenResponse {

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private int refresh_expires_in;
    private String token_type;
    private String scope;
    private String session_state;
}
