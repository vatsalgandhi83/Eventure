package com.eventure.events.dto.auth.outbound;

import lombok.Data;
import java.util.List;
import lombok.Builder;

@Data
@Builder
public class KeycloakUserRequest {

    private String username;
    private String email;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private List<Credential> credentials;

    @Data
    @Builder
    public static class Credential {
        private String type;
        private String value;
        private boolean temporary;
    }
}
