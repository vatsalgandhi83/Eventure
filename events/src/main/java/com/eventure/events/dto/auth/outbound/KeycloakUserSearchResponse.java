package com.eventure.events.dto.auth.outbound;

import lombok.Data;
import java.util.List;

@Data
public class KeycloakUserSearchResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private long createdTimestamp;
    private boolean enabled;
    private boolean totp;
    private List<String> disableableCredentialTypes;
    private List<String> requiredActions;
    private int notBefore;
    private Access access;

    @Data
    public static class Access {
        private boolean manageGroupMembership;
        private boolean view;
        private boolean mapRoles;
        private boolean impersonate;
        private boolean manage;
    }
} 