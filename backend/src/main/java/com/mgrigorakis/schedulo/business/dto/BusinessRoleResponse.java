package com.mgrigorakis.schedulo.business.dto;

import java.time.LocalDateTime;

public record BusinessRoleResponse(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
