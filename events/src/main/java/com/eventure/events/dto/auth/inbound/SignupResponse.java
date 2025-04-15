package com.eventure.events.dto.auth.inbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
    private String message;
    private String status;
    private String details; // Optional, used for error details
} 