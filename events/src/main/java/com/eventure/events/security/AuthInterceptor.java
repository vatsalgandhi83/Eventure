package com.eventure.events.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.eventure.events.Services.AuthService;
import com.eventure.events.dto.auth.inbound.TokenRefreshResponse;
import com.eventure.events.dto.auth.inbound.TokenValidationRequest;
import com.eventure.events.dto.auth.inbound.TokenValidationResponse;

import javax.servlet.http.*;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow OPTIONS requests to pass through without authentication
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        // Check if the request is for the signup or signin endpoint
        if (path.startsWith("api/auth")) {
            return true; // Allow access to signup and signin endpoints without token validation
        }
        
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing");
            return false;
        }

        // Remove "Bearer " prefix if present
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        TokenValidationResponse tokenValidationResponse = authService.validateAccessToken(TokenValidationRequest.builder()
                .token(accessToken)
                .build());
        
        if (tokenValidationResponse.isValid()) {
            // Token is valid, proceed with the request
            return true;
        }

        String refreshToken = authService.fetchRefreshToken(request);

        if (refreshToken != null) {
            TokenRefreshResponse tokenRefreshResponse = authService.refreshAccessToken(refreshToken);
            if (tokenRefreshResponse.getAccessToken() != null) {
                // The Token is expired, but the refresh token is valid, send the new access token in the response
                response.setHeader("New-Access-Token", tokenRefreshResponse.getAccessToken());
                if (tokenRefreshResponse.getRefreshToken() != null) {
                    // Keycloak rotated the **refresh token**, so we need to update the cookie
                    ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenRefreshResponse.getRefreshToken())
                            .httpOnly(true).secure(true).path("/").maxAge(tokenRefreshResponse.getRefreshExpiresIn()).sameSite("Lax").build();
                    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                }
                return true;
            }
        }

        // Both access and refresh tokens are invalid, send an error response
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
        return false;
    }
}
