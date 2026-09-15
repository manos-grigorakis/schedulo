package com.mgrigorakis.schedulo.common.exception;

import com.mgrigorakis.schedulo.common.dto.ApiResponseWrapper;
import com.mgrigorakis.schedulo.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Field validation error - 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleFieldValidationException(MethodArgumentNotValidException exc) {
        Map<String, String> details = new HashMap<>();

        exc.getBindingResult().getFieldErrors()
                .forEach(error -> details.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Field Validation Failed",
                details
        );

        return new ResponseEntity<>(new ApiResponseWrapper<>(response), HttpStatus.BAD_REQUEST);
    }

    // Bad credentials - 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleBadCredentialsException(BadCredentialsException exc) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid email or password",
                "INVALID_CREDENTIALS",
                null
        );

        return new ResponseEntity<>(new ApiResponseWrapper<>(response), HttpStatus.UNAUTHORIZED);
    }

    // Forbidden - 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleAccessDeniedException(AccessDeniedException exc) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                exc.getMessage(),
                null
        );

        return new ResponseEntity<>(new ApiResponseWrapper<>(response), HttpStatus.FORBIDDEN);
    }

    // Not Found - 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleResourceNotFoundException(ResourceNotFoundException exc) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),
                null
        );

        return new ResponseEntity<>(new ApiResponseWrapper<>(response), HttpStatus.NOT_FOUND);
    }

    // User already exists - 409
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException exc) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Unable to complete registration",
                "REGISTRATION_CONFLICT",
                null
        );

        return new ResponseEntity<>(new ApiResponseWrapper<>(response), HttpStatus.CONFLICT);
    }

    // Server error - 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleException(Exception exc) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                null
        );

        log.error("An unexpected error occurred", exc);
        return new ResponseEntity<>(new ApiResponseWrapper<>(response), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Storage Service Exception - 503
    @ExceptionHandler(StorageServiceException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleStorageServiceException(StorageServiceException exc) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                exc.getMessage(),
                null
        );

        return new ResponseEntity<>(new ApiResponseWrapper<>(response), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
