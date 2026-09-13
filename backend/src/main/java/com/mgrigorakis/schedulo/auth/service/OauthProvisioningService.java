package com.mgrigorakis.schedulo.auth.service;


import com.mgrigorakis.schedulo.users.enums.OauthProvider;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface OauthProvisioningService {
    void provisionIfNecessary(OidcUser oidcUser, OauthProvider provider);
}
