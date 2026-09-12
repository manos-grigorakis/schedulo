package com.mgrigorakis.schedulo.auth.service;

import com.mgrigorakis.schedulo.auth.mapper.AuthMapper;
import com.mgrigorakis.schedulo.common.exception.DefaultPlatformRoleNotFound;
import com.mgrigorakis.schedulo.common.exception.UserAlreadyExistsException;
import com.mgrigorakis.schedulo.users.enums.OauthProvider;
import com.mgrigorakis.schedulo.users.model.OauthAccount;
import com.mgrigorakis.schedulo.users.model.PlatformRole;
import com.mgrigorakis.schedulo.users.model.User;
import com.mgrigorakis.schedulo.users.repository.OauthAccountRepository;
import com.mgrigorakis.schedulo.users.repository.PlatformRoleRepository;
import com.mgrigorakis.schedulo.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthProvisioningServiceImpl implements OauthProvisioningService {
    private final UserRepository userRepository;
    private final PlatformRoleRepository platformRoleRepository;
    private final OauthAccountRepository oauthAccountRepository;
    private final AuthMapper authMapper;

    @Transactional
    @Override
    public void provisionIfNecessary(OidcUser oidcUser) {
        if (oauthAccountRepository.existsByProviderAndExternalSubject(OauthProvider.GOOGLE, oidcUser.getSubject())) {
            return;
        }

        if (userRepository.existsByEmail(oidcUser.getEmail())) {
            // Account exists but is not linked with the provider
            log.warn("Registration attempted with an existing email");
            throw new UserAlreadyExistsException();
        }

        PlatformRole role = platformRoleRepository.findByName("USER").orElseThrow(() -> {
            log.error("Default platform role USER not found during user registration");
            return new DefaultPlatformRoleNotFound("USER");
        });

        User user = authMapper.toUserFromOidcUser(oidcUser);
        user.setPlatformRole(role);
        userRepository.save(user);

        OauthAccount oauthAccount = OauthAccount.builder()
                .user(user)
                .provider(OauthProvider.GOOGLE)
                .externalSubject(oidcUser.getSubject())
                .build();
        oauthAccountRepository.save(oauthAccount);
    }
}
