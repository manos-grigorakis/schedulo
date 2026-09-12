package com.mgrigorakis.schedulo.auth.service;


import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface OauthProvisioningService {
    void provisionIfNecessary(OidcUser oidcUser);
}
