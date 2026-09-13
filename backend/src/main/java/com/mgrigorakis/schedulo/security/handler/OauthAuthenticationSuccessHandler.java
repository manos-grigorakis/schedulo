package com.mgrigorakis.schedulo.security.handler;

import com.mgrigorakis.schedulo.security.service.JwtService;
import com.mgrigorakis.schedulo.users.enums.OauthProvider;
import com.mgrigorakis.schedulo.users.model.OauthAccount;
import com.mgrigorakis.schedulo.users.model.User;
import com.mgrigorakis.schedulo.users.repository.OauthAccountRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final OauthAccountRepository oauthAccountRepository;
    private final JwtService jwtService;

    @Value("${spring.security.jwt.cookie-name}")
    private String cookieName;

    @Value("${spring.security.jwt.expiration-in-ms}")
    private Long jwtExpirationInMs;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        OauthAccount oauthAccount = oauthAccountRepository.findByProviderAndExternalSubject(
                OauthProvider.GOOGLE, oidcUser.getSubject()).orElseThrow(() ->
                                                                                 new ServletException(
                                                                                         "OauthAccount not found")
        );

        User user = oauthAccount.getUser();
        String token = jwtService.generateToken(user.getId(), user.getPlatformRole().getName());

        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofMillis(jwtExpirationInMs))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
