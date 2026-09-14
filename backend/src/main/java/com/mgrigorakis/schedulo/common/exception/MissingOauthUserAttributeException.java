package com.mgrigorakis.schedulo.common.exception;

public class MissingOauthUserAttributeException extends RuntimeException {
    public MissingOauthUserAttributeException(String provider, String attribute) {
        super("Missing required OauthUser attribute: " + attribute + ", for provider: " + provider);
    }
}
