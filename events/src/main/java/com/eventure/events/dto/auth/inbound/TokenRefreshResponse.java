package com.eventure.events.dto.auth.inbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRefreshResponse {
    private String refreshToken;
    private int refreshExpiresIn;
    private String accessToken;
} 