package com.mgrigorakis.schedulo.common.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OauthUserAlreadyExistsException extends OAuth2AuthenticationException {
    public OauthUserAlreadyExistsException() {
        super("User already exists");
    }
}
