package com.mgrigorakis.schedulo.security.config;

import com.mgrigorakis.schedulo.security.service.CustomOidcUserService;
import com.mgrigorakis.schedulo.security.handler.OauthAuthenticationSuccessHandler;
import com.mgrigorakis.schedulo.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final CustomOidcUserService customOidcUserService;
    private final OauthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF due to stateless session for JWT
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Stateless session for JWT
                .sessionManagement(session ->
                                           session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Custom Authentication provider
                .authenticationProvider(authenticationProvider())

                // JWT filter before Spring Security default filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // OAuth2 Login
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfo) -> userInfo.oidcUserService(customOidcUserService))
                        .successHandler(oauthAuthenticationSuccessHandler)
                )
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Custom Authentication provider,
     * links {@link UserDetailsService} and {@link PasswordEncoder}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * Authentication manager bean for programmatic authentication
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
