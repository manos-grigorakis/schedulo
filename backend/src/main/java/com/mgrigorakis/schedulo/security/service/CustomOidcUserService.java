package com.mgrigorakis.schedulo.security.service;

import com.mgrigorakis.schedulo.auth.service.OauthProvisioningService;
import com.mgrigorakis.schedulo.users.enums.OauthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final OidcUserService delegate = new OidcUserService();
    private final OauthProvisioningService oauthProvisioningService;

    @Override
    public @Nullable OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegate to the default implementation for loading a user
        OidcUser oidcUser = delegate.loadUser(userRequest);

        oauthProvisioningService.provisionIfNecessary(oidcUser, getOauthProvider(userRequest));

        return oidcUser;
    }

    private OauthProvider getOauthProvider(OidcUserRequest userRequest) {
        return OauthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
    }
}
