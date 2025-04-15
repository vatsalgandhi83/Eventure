package com.eventure.events.dto.auth.inbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private String userId;
    private String role;
    private String message; // Optional, used for error messages
    private String status; // Optional, used for error status
} 