package com.mgrigorakis.schedulo.auth.service;

import com.mgrigorakis.schedulo.auth.dto.LoginResponse;
import com.mgrigorakis.schedulo.auth.model.UserInfoDetails;
import com.mgrigorakis.schedulo.auth.dto.LoginRequest;
import com.mgrigorakis.schedulo.auth.dto.RegistrationRequest;
import com.mgrigorakis.schedulo.common.exception.DefaultPlatformRoleNotFound;
import com.mgrigorakis.schedulo.common.exception.UserAlreadyExistsException;
import com.mgrigorakis.schedulo.security.jwt.JwtService;
import com.mgrigorakis.schedulo.users.model.PlatformRole;
import com.mgrigorakis.schedulo.users.model.User;
import com.mgrigorakis.schedulo.users.repository.PlatformRoleRepository;
import com.mgrigorakis.schedulo.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PlatformRoleRepository platformRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(user.getId(), user.getAuthorities().iterator().next().getAuthority());
        return new LoginResponse(token);
    }

    @Override
    public void registration(RegistrationRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            log.warn("Registration attempted with an existing email");
            throw new UserAlreadyExistsException();
        });

        String hashedPassword = passwordEncoder.encode(request.password());

        // TODO: Use mapper instead
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(hashedPassword)
                .phone(request.phone())
                .build();

        PlatformRole role = platformRoleRepository.findByName("USER").orElseThrow(() -> {
            log.error("Default platform role USER not found during user registration");
            return new DefaultPlatformRoleNotFound("USER");
        });

        user.setPlatformRole(role);
        userRepository.save(user);
    }
}
