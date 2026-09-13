package com.mgrigorakis.schedulo.auth;

import com.mgrigorakis.schedulo.auth.service.UserInfoService;
import com.mgrigorakis.schedulo.users.model.PlatformRole;
import com.mgrigorakis.schedulo.users.model.User;
import com.mgrigorakis.schedulo.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTest {
    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserRepository userRepository;

    private final String mockEmail = "test@example.com";

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // Arrange
        User user = User.builder().email(mockEmail).build();
        PlatformRole platformRole = PlatformRole.builder().name("USER").build();
        user.setPlatformRole(platformRole);

        when(userRepository.findByEmail(mockEmail)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userInfoService.loadUserByUsername(mockEmail);

        // Assert
        assertEquals(mockEmail, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(mockEmail);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        // Assert
        when(userRepository.findByEmail(mockEmail)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userInfoService.loadUserByUsername(mockEmail));
        verify(userRepository, times(1)).findByEmail(mockEmail);
    }
}
