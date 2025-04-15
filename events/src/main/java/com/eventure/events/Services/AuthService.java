package com.eventure.events.Services;

import com.eventure.events.dto.auth.inbound.SignupRequest;
import com.eventure.events.dto.auth.inbound.SignupResponse;
import com.eventure.events.dto.auth.outbound.KeycloakUserRequest;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class AuthService {
    
    private final KeycloakService keycloakService;
    
    public AuthService(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }
    
    public SignupResponse signup(SignupRequest request) {
        try {
            System.out.println("Starting user registration process in Keycloak =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request);
            
            // 1. Create user in Keycloak
            KeycloakUserRequest userRequest = new KeycloakUserRequest();
            userRequest.setUsername(request.getUsername());
            userRequest.setFirstName(request.getFirstName());
            userRequest.setLastName(request.getLastName());
            userRequest.setEmail(request.getEmail());
            userRequest.setEnabled(true);
            
            KeycloakUserRequest.Credential credential = new KeycloakUserRequest.Credential();
            credential.setType("password");
            credential.setValue(request.getPassword());
            credential.setTemporary(false);
            userRequest.setCredentials(Collections.singletonList(credential));
            
            System.out.println("Creating user in Keycloak =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + userRequest);
            String userId = keycloakService.createUser(userRequest);
            System.out.println("User created in Keycloak with ID =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + userId);
            
            // 2. Get role ID
            System.out.println("Getting role ID for role =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request.getRole());
            String roleId = keycloakService.getRoleId(request.getRole());
            System.out.println("Retrieved role ID =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + roleId);
            
            // 3. Assign role to user
            System.out.println("Assigning role to user =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            keycloakService.assignRole(userId, roleId, request.getRole());
            System.out.println("Role assigned successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            
            return new SignupResponse("User registered successfully", "success", null);
        } catch (Exception e) {
            System.out.println("Error during user registration =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
            return new SignupResponse("User registration failed", "error", e.getMessage());
        }
    }
} 