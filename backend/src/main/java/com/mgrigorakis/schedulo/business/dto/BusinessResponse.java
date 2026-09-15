package com.mgrigorakis.schedulo.business.dto;

import java.time.LocalDateTime;

public record BusinessResponse(
        Long id,
        String name,
        String slug,
        String email,
        String logoUrl,
        String phone,
        String street,
        String streetNumber,
        String postalCode,
        String city,
        String country,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
