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
} 