package com.mgrigorakis.schedulo.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record BusinessRequest(
        @NotBlank(message = "Name is required")
        String name,

        @Email
        @NotBlank(message = "Email is required")
        @Size(max = 320)
        String email,

        MultipartFile logo,

        @NotBlank(message = "Phone is required")
        @Size(max = 30)
        String phone,

        @NotBlank(message = "Street is required")
        @Size(max = 30)
        String street,

        @NotBlank(message = "Street number is required")
        @Size(max = 10)
        String streetNumber,

        @NotBlank(message = "Postal code is required")
        @Size(max = 20)
        String postalCode,

        @NotBlank(message = "City is required")
        @Size(max = 40)
        String city,

        @NotBlank(message = "Country is required")
        @Size(max = 40)
        String country
) {
}
