package com.eventure.events.dto.auth.inbound;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
public class SignupRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone Number is Required")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", 
            message = "Password must be at least 8 characters long and contain at least one letter and one number")
    private String password;
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(Customer|Manager)$", message = "Role must be either Customer or Manager")
    private String role;
} 