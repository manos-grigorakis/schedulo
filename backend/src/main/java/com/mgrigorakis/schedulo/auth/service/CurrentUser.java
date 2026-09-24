package com.mgrigorakis.schedulo.auth.service;

import com.mgrigorakis.schedulo.common.exception.AuthenticationCredentialsNotFoundException;
import com.mgrigorakis.schedulo.users.model.User;
import com.mgrigorakis.schedulo.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CurrentUser {
    private final UserRepository userRepository;

    /**
     * Returns the current authenticated user
     *
     * @return The authenticated user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }

        Long id = Long.valueOf(authentication.getName());

        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with id {}", id);
            return new UsernameNotFoundException("User not found with id " + id);
        });
    }
}
