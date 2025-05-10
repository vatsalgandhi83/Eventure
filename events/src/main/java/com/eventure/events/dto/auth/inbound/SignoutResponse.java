package com.eventure.events.dto.auth.inbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignoutResponse {
    private String message;
    private String status;
} 