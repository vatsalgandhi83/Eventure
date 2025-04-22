package com.eventure.events.Services;

import com.eventure.events.dto.auth.inbound.SigninRequest;
import com.eventure.events.dto.auth.inbound.SigninResponse;
import com.eventure.events.dto.auth.inbound.SignupRequest;
import com.eventure.events.dto.auth.inbound.SignupResponse;
import com.eventure.events.dto.auth.inbound.SignoutRequest;
import com.eventure.events.dto.auth.inbound.SignoutResponse;
import com.eventure.events.dto.auth.inbound.TokenValidationRequest;
import com.eventure.events.dto.auth.inbound.TokenValidationResponse;
import com.eventure.events.dto.auth.outbound.KeycloakUserRequest;
import com.eventure.events.dto.auth.outbound.KeycloakGetTokenResponse;
import com.eventure.events.dto.auth.outbound.KeycloakGetTokenRequest;
import com.eventure.events.dto.auth.inbound.TokenRefreshResponse;
import org.springframework.stereotype.Service;
import java.util.Collections;
import com.eventure.events.Services.UserService;
import com.eventure.events.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import com.eventure.events.dto.auth.outbound.KeycloakIntrospectTokenResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    private final KeycloakService keycloakService;
    
    public AuthService(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }
    
   public SignupResponse signup(SignupRequest request) {
        try {
            System.out.println("Starting user registration process in Keycloak =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request);
            
            // 1. Create user in Keycloak
            KeycloakUserRequest userRequest = KeycloakUserRequest.builder()
            .username(request.getUsername())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .enabled(true)
            .credentials(Collections.singletonList(
                KeycloakUserRequest.Credential.builder()
                .type("password")
                .value(request.getPassword())
                .temporary(false)
                .build()
            ))
            .build();
            
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

            // 4. Add user to Database
            userService.addNewUser(Users.builder()
                .userId(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNo(request.getPhoneNumber())
                .usertype(request.getRole())
                .build()
            );
            
            return new SignupResponse("User registered successfully", "success", null);
        } catch (Exception e) {
            System.out.println("Error during user registration =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
            return new SignupResponse("User registration failed", "error", e.getMessage());
        }
    }

    public SigninResponse signin(SigninRequest request) {
        try {
            System.out.println("Starting user login process in Keycloak =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request);
            KeycloakGetTokenResponse tokenResponse = keycloakService.getToken(KeycloakGetTokenRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .grantType("password")
                .build());
            System.out.println("Token retrieved successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + tokenResponse);

//            String refreshToken = tokenResponse.getRefresh_token();

            return SigninResponse.builder()
                .message("User login successful")
                .status("success")
                .accessToken(tokenResponse.getAccess_token())
                .refreshToken(tokenResponse.getRefresh_token())
                .refreshExpiresIn(tokenResponse.getRefresh_expires_in())
                .build();


        } catch (Exception e) {
            System.out.println("Error during user login =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
            return SigninResponse.builder()
                .message("User login failed")
                .status("error")
                .build();
        }
    }

    public void signout(SignoutRequest request) {
        try {
            System.out.println("Starting user logout process in Keycloak =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + request);
            keycloakService.logout(request.getRefreshToken());
            System.out.println("User logged out successfully =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        } catch (Exception e) {
            System.out.println("Error during user logout =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
        }
    }

    public TokenValidationResponse validateAccessToken(TokenValidationRequest request) {
        try {
            KeycloakIntrospectTokenResponse tokenResponse = keycloakService.introspectToken(request.getToken());
            System.out.println("Token introspection response =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + tokenResponse);
            if (tokenResponse.isActive()) {
                return TokenValidationResponse.builder()
                .valid(true)
                .build();
            } else {
                return TokenValidationResponse.builder()
                .valid(false)

                .build();
            }
        } catch (Exception e) {
            System.out.println("Error during token validation =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
            return TokenValidationResponse.builder()
            .valid(false)
            .build();
        }
    }

    public TokenRefreshResponse refreshAccessToken(String refreshToken) {
        System.out.println("Starting token refresh process =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + refreshToken);
        try {

            KeycloakGetTokenResponse tokenResponse = keycloakService.getToken(KeycloakGetTokenRequest.builder()
                .refreshToken(refreshToken)
                .grantType("refresh_token")
                .build());
            return TokenRefreshResponse.builder()
                .refreshToken(tokenResponse.getRefresh_token())
                .accessToken(tokenResponse.getAccess_token())
                .refreshExpiresIn(tokenResponse.getRefresh_expires_in())
                .build();
            
        } catch (Exception e) {
            return TokenRefreshResponse.builder()
                .build();
        }
    }

    public String fetchRefreshToken(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie clearedCookie = new Cookie("refreshToken", null);
        clearedCookie.setHttpOnly(true);
        clearedCookie.setPath("/");
        clearedCookie.setMaxAge(0);
        response.addCookie(clearedCookie);
    }

}