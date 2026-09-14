package com.mgrigorakis.schedulo.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseWrapper<T>(String transactionId, T data, LocalDateTime timestamp, ErrorResponse error) {
    public ApiResponseWrapper {
        if (transactionId == null) transactionId = UUID.randomUUID().toString();
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    public ApiResponseWrapper(T data) {
        this(null, data, null, null);
    }

    public ApiResponseWrapper(ErrorResponse error) {
        this(null, null, null, error);
    }
}
