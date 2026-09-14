package com.mgrigorakis.schedulo.auth;

import com.mgrigorakis.schedulo.auth.dto.LoginRequest;
import com.mgrigorakis.schedulo.auth.dto.RegistrationRequest;
import com.mgrigorakis.schedulo.auth.mapper.AuthMapper;
import com.mgrigorakis.schedulo.auth.model.UserInfoDetails;
import com.mgrigorakis.schedulo.auth.service.AuthServiceImpl;
import com.mgrigorakis.schedulo.common.exception.DefaultPlatformRoleNotFound;
import com.mgrigorakis.schedulo.common.exception.UserAlreadyExistsException;
import com.mgrigorakis.schedulo.security.service.JwtService;
import com.mgrigorakis.schedulo.users.model.PlatformRole;
import com.mgrigorakis.schedulo.users.model.User;
import com.mgrigorakis.schedulo.users.repository.PlatformRoleRepository;
import com.mgrigorakis.schedulo.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlatformRoleRepository platformRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthMapper authMapper;

    private final String mockEmail = "test@example.com";
    private final RegistrationRequest registrationRequest = new RegistrationRequest(
            null, null, mockEmail, "test", null);

    @Test
    void login_shouldGenerateJwtToken_whenUserAuthenticates() {
        // Arrange
        LoginRequest request = new LoginRequest(mockEmail, "test");
        UserInfoDetails userInfoDetails = mock(UserInfoDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userInfoDetails);
        when(userInfoDetails.getId()).thenReturn(1L);
        doReturn(List.of(new SimpleGrantedAuthority("USER"))).when(userInfoDetails).getAuthorities();
        when(jwtService.generateToken(1L, "USER")).thenReturn("token");

        // Act
        authService.login(request);

        // Assert
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(1L, "USER");
    }

    @Test
    void registration_shouldRegisterUser_whenEmailDoesNotExist() {
        // Arrange
        User user = User.builder().email(mockEmail).build();
        PlatformRole role = PlatformRole.builder().name("USER").build();

        when(userRepository.findByEmail(mockEmail)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("test");
        when(authMapper.toUserFromRegistration(any(), any())).thenReturn(user);
        when(platformRoleRepository.findByName("USER")).thenReturn(Optional.of(role));

        // Act
        authService.registration(registrationRequest);

        // Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals("USER", savedUser.getPlatformRole().getName());
    }

    @Test
    void registration_shouldThrowUserAlreadyExistsException_whenUserExists() {
        // Arrange
        User user = User.builder().email(mockEmail).build();

        when(userRepository.findByEmail(mockEmail)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> authService.registration(registrationRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registration_shouldThrowDefaultPlatformRoleNotFoundException_whenPlatformRoleDoesNotExist() {
        // Arrange
        User user = User.builder().email(mockEmail).build();

        when(userRepository.findByEmail(mockEmail)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("test");
        when(authMapper.toUserFromRegistration(any(), any())).thenReturn(user);
        when(platformRoleRepository.findByName("USER")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DefaultPlatformRoleNotFound.class, () -> authService.registration(registrationRequest));
        verify(userRepository, never()).save(any());
    }
}
