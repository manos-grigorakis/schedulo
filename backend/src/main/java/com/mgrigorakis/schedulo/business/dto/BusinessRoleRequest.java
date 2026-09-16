package com.mgrigorakis.schedulo.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BusinessRoleRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 50)
        String name,

        @Size(max = 500)
        String description
) {
}
