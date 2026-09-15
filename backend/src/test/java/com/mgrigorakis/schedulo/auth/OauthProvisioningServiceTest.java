package com.mgrigorakis.schedulo.auth;

import com.mgrigorakis.schedulo.auth.mapper.AuthMapper;
import com.mgrigorakis.schedulo.auth.service.OauthProvisioningServiceImpl;
import com.mgrigorakis.schedulo.common.exception.DefaultPlatformRoleNotFound;
import com.mgrigorakis.schedulo.common.exception.OauthUserAlreadyExistsException;
import com.mgrigorakis.schedulo.common.exception.UserAlreadyExistsException;
import com.mgrigorakis.schedulo.users.enums.OauthProvider;
import com.mgrigorakis.schedulo.users.model.OauthAccount;
import com.mgrigorakis.schedulo.users.model.PlatformRole;
import com.mgrigorakis.schedulo.users.model.User;
import com.mgrigorakis.schedulo.users.repository.OauthAccountRepository;
import com.mgrigorakis.schedulo.users.repository.PlatformRoleRepository;
import com.mgrigorakis.schedulo.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OauthProvisioningServiceTest {
    @InjectMocks
    private OauthProvisioningServiceImpl oauthProvisioningService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlatformRoleRepository platformRoleRepository;

    @Mock
    private OauthAccountRepository oauthAccountRepository;

    @Mock
    private AuthMapper authMapper;

    private final String mockEmail = "test@example.com";

    @Test
    void provisioning_shouldProvisionUser() {
        // Arrange
        OidcUser oidcUser = mock(OidcUser.class);
        PlatformRole platformRole = PlatformRole.builder().name("USER").build();
        User user = User.builder().email(mockEmail).build();

        when(oidcUser.getSubject()).thenReturn("random-sub");
        when(oidcUser.getEmail()).thenReturn(mockEmail);
        when(oauthAccountRepository.existsByProviderAndExternalSubject(OauthProvider.GOOGLE, "random-sub"))
                .thenReturn(false);
        when(userRepository.existsByEmail(mockEmail)).thenReturn(false);
        when(platformRoleRepository.findByName("USER")).thenReturn(Optional.of(platformRole));
        when(authMapper.toUserFromOidcUser(oidcUser)).thenReturn(user);

        // Act
        oauthProvisioningService.provisionIfNecessary(oidcUser, OauthProvider.GOOGLE);

        // Assert
        ArgumentCaptor<OauthAccount> captor = ArgumentCaptor.forClass(OauthAccount.class);
        verify(oauthAccountRepository).save(captor.capture());

        OauthAccount savedAccount = captor.getValue();
        assertEquals(user, savedAccount.getUser());
        assertEquals(OauthProvider.GOOGLE, savedAccount.getProvider());
        assertEquals("random-sub", savedAccount.getExternalSubject());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void provisioning_shouldNotProvisionUser_whenUserExistsByTheSameProviderAndExternalSubject() {
        // Arrange
        OidcUser oidcUser = mock(OidcUser.class);
        when(oidcUser.getSubject()).thenReturn("random-sub");
        when(oauthAccountRepository.existsByProviderAndExternalSubject(OauthProvider.GOOGLE, "random-sub"))
                .thenReturn(true);

        // Act
        oauthProvisioningService.provisionIfNecessary(oidcUser, OauthProvider.GOOGLE);

        // Assert
        verify(userRepository, never()).save(any());
        verify(oauthAccountRepository, never()).save(any());
    }

    @Test
    void provisioning_shouldThrowOauthUserAlreadyExistsException_whenEmailAlreadyExists() {
        // Arrange
        OidcUser oidcUser = mock(OidcUser.class);
        when(oidcUser.getSubject()).thenReturn("random-sub");
        when(oidcUser.getEmail()).thenReturn(mockEmail);

        when(oauthAccountRepository.existsByProviderAndExternalSubject(OauthProvider.GOOGLE, "random-sub"))
                .thenReturn(false);
        when(userRepository.existsByEmail(mockEmail)).thenReturn(true);


        // Act & Assert
        assertThrows(OauthUserAlreadyExistsException.class, () ->
                oauthProvisioningService.provisionIfNecessary(oidcUser, OauthProvider.GOOGLE));
        verify(userRepository, never()).save(any());
        verify(oauthAccountRepository, never()).save(any());
    }

    @Test
    void provisioning_shouldThrowDefaultPlatformRoleNotFoundException_whenDefaultPlatformRoleNotFound() {
        // Arrange
        OidcUser oidcUser = mock(OidcUser.class);

        when(oidcUser.getSubject()).thenReturn("random-sub");
        when(oidcUser.getEmail()).thenReturn(mockEmail);
        when(oauthAccountRepository.existsByProviderAndExternalSubject(OauthProvider.GOOGLE, "random-sub"))
                .thenReturn(false);
        when(userRepository.existsByEmail(mockEmail)).thenReturn(false);
        when(platformRoleRepository.findByName("USER")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DefaultPlatformRoleNotFound.class, () ->
                oauthProvisioningService.provisionIfNecessary(oidcUser, OauthProvider.GOOGLE));

        verify(userRepository, never()).save(any());
        verify(oauthAccountRepository, never()).save(any());
    }
}
