package com.eventure.events.dto.auth.outbound;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor               // Jackson needs this
@AllArgsConstructor              // for completeness / builder
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakIntrospectTokenResponse {
    @JsonProperty("active")
    private boolean active;

    @JsonProperty("scope")
    private String scope;

    // match JSON "realm_access"
    @JsonProperty("realm_access")
    private RealmAccess realmAccess;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    @JsonProperty("name")
    private String name;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("token_type")
    private String tokenType;

    // Nested class for "realm_access"
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RealmAccess {
        private List<String> roles;
    }
}
