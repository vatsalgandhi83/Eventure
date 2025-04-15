package com.eventure.events.dto.auth.outbound;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakRoleAssignmentRequest {
    private String id;
    private String name;
} 