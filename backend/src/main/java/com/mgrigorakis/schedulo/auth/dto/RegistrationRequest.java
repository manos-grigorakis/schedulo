package com.mgrigorakis.schedulo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 120)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 120)
        String lastName,

        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                message = "Password must be 8-20 characters and include uppercase, lowercase, number and special character"
        )
        String password,

        @Size(max = 30)
        String phone
) {
}
