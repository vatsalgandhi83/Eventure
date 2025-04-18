package com.eventure.events.dto.auth.inbound;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SigninResponse {
    private String accessToken;
    private String message;
    private String status;

    @JsonIgnore
    private String refreshToken;
    @JsonIgnore
    private int refreshExpiresIn;

} 