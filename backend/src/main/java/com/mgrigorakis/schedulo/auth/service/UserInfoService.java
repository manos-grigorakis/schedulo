package com.mgrigorakis.schedulo.auth.service;

import com.mgrigorakis.schedulo.auth.model.UserInfoDetails;
import com.mgrigorakis.schedulo.users.model.User;
import com.mgrigorakis.schedulo.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> {
            log.warn("User not found with email: {}", username);
            return new UsernameNotFoundException("User not found with email:" + username);
        });

        return new UserInfoDetails(user);
    }
}
