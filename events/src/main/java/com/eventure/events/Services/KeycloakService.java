package com.eventure.events.Services;

import com.eventure.events.dto.auth.outbound.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.client.id}")
    private String clientId;

    @Value("${keycloak.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getAdminToken() {
        System.out.println("Getting admin token from Keycloak =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String tokenUrl = keycloakUrl + "/realms/master/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format(
                "client_id=admin-cli&username=%s&password=%s&grant_type=password",
                adminUsername, adminPassword
        );

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<KeycloakTokenResponse> response = restTemplate.postForEntity(
                tokenUrl, request, KeycloakTokenResponse.class
        );

        System.out.println("Admin token retrieved successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return response.getBody().getAccess_token();
    }

    public String createUser(KeycloakUserRequest userRequest) {
        System.out.println("Creating user in Keycloak =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + userRequest);
        String adminToken = getAdminToken();
        String createUserUrl = keycloakUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        HttpEntity<KeycloakUserRequest> request = new HttpEntity<>(userRequest, headers);
        restTemplate.postForEntity(createUserUrl, request, Void.class);

        // Get user ID using search API
        String userId = getUserIdByUsername(userRequest.getUsername());
        System.out.println("User created successfully in Keycloak with ID =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + userId);
        return userId;
    }

    private String getUserIdByUsername(String username) {
        System.out.println("Getting user ID for username =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + username);
        String adminToken = getAdminToken();
        String searchUrl = keycloakUrl + "/admin/realms/" + realm + "/users?username=" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<KeycloakUserSearchResponse[]> response = restTemplate.exchange(
                searchUrl, HttpMethod.GET, request, KeycloakUserSearchResponse[].class
        );

        KeycloakUserSearchResponse[] users = response.getBody();
        if (users != null && users.length > 0) {
            System.out.println("User ID retrieved successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + users[0].getId());
            return users[0].getId();
        }
        System.out.println("User not found after creation =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        throw new RuntimeException("User not found after creation");
    }

    public String getRoleId(String roleName) {
        System.out.println("Getting role ID for role =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + roleName);
        String adminToken = getAdminToken();
        String roleUrl = keycloakUrl + "/admin/realms/" + realm + "/roles/" + roleName;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<KeycloakRoleResponse> response = restTemplate.exchange(
                roleUrl, HttpMethod.GET, request, KeycloakRoleResponse.class
        );

        String roleId = response.getBody().getId();
        System.out.println("Role ID retrieved successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + roleId);
        return roleId;
    }

    public void assignRole(String userId, String roleId, String roleName) {
        System.out.println("Assigning role to user =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
                "userId: " + userId + ", roleId: " + roleId + ", roleName: " + roleName);
        String adminToken = getAdminToken();
        String assignRoleUrl = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        KeycloakRoleAssignmentRequest role = KeycloakRoleAssignmentRequest.builder()
                .id(roleId)
                .name(roleName)
                .build();

        HttpEntity<List<KeycloakRoleAssignmentRequest>> request = new HttpEntity<>(Collections.singletonList(role), headers);
        restTemplate.postForEntity(assignRoleUrl, request, Void.class);
        System.out.println("Role assigned successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    public KeycloakGetTokenResponse getToken(KeycloakGetTokenRequest request) {
        System.out.println("Getting token for user =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request.getUsername());
        String tokenUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        // 1) Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2) Build the form‑encoded body string
        // start with common params
        StringBuilder body = new StringBuilder();
        body.append("client_id=").append(clientId)
                .append("&client_secret=").append(clientSecret)
                .append("&grant_type=").append(request.getGrantType());

        // add either username/password or refresh_token
        if ("password".equals(request.getGrantType())) {
            body.append("&username=").append(request.getUsername())
                    .append("&password=").append(request.getPassword());
        } else if ("refresh_token".equals(request.getGrantType())) {
            body.append("&refresh_token=").append(request.getRefreshToken());
        }

        // 3) Wrap in HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(body.toString(), headers);

        // 4) Call Keycloak
        ResponseEntity<KeycloakGetTokenResponse> response = restTemplate.postForEntity(
                tokenUrl, requestEntity, KeycloakGetTokenResponse.class
        );

        System.out.println("Token retrieved successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.getBody());
        return response.getBody();
    }


    public KeycloakIntrospectTokenResponse introspectToken(String token) {

        System.out.println("Introspecting token =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + token);
        String introspectUrl = keycloakUrl
                + "/realms/" + realm
                + "/protocol/openid-connect/token/introspect";

        // 1) Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2) Build the form‑encoded body string
        // Note: URLEncoder is optional if your token/clientId/secret have no special
        // characters that need encoding—but feel free to encode if you like.
        String body = String.format(
                "token=%s&client_id=%s&client_secret=%s",
                token,
                clientId,
                clientSecret
        );
        System.out.println("Introspecting token body =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + body);
        // 3) Wrap in HttpEntity<String>
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // 4) Call Keycloak introspection endpoint
        ResponseEntity<KeycloakIntrospectTokenResponse> response = restTemplate
                .postForEntity(introspectUrl, request, KeycloakIntrospectTokenResponse.class);

        System.out.println("Token introspected successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                + response.getBody());
        return response.getBody();
    }

    public void logout(String refreshToken) {
        System.out.println("Logging out user =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + refreshToken);
        String logoutUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        // 1) Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2) Build the form‑encoded body string
        String body = String.format(
                "client_id=%s&client_secret=%s&refresh_token=%s",
                clientId,
                clientSecret,
                refreshToken
        );

        // 3) Wrap in HttpEntity<String>
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // 4) Call Keycloak logout endpoint
        restTemplate.postForEntity(logoutUrl, request, Void.class);
        System.out.println("User logged out successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

} 