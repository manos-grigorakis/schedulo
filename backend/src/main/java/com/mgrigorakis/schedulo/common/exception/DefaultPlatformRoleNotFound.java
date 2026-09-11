package com.mgrigorakis.schedulo.common.exception;

public class DefaultPlatformRoleNotFound extends RuntimeException {
    public DefaultPlatformRoleNotFound(String role) {
        super("Default platform role not found: " + role);
    }
}
