package com.mgrigorakis.schedulo.common.exception;

public class AuthenticationCredentialsNotFoundException extends RuntimeException {
    public AuthenticationCredentialsNotFoundException(String message) {
        super(message);
    }
}
