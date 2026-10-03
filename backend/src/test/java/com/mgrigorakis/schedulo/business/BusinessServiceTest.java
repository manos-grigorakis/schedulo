package com.mgrigorakis.schedulo.business;

import com.mgrigorakis.schedulo.auth.service.CurrentUser;
import com.mgrigorakis.schedulo.business.dto.BusinessRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessResponse;
import com.mgrigorakis.schedulo.business.mapper.BusinessMapper;
import com.mgrigorakis.schedulo.business.model.Business;
import com.mgrigorakis.schedulo.business.model.BusinessRole;
import com.mgrigorakis.schedulo.business.repository.BusinessMemberRepository;
import com.mgrigorakis.schedulo.business.repository.BusinessRepository;
import com.mgrigorakis.schedulo.business.repository.BusinessRoleRepository;
import com.mgrigorakis.schedulo.business.service.BusinessServiceImpl;
import com.mgrigorakis.schedulo.common.exception.ResourceNotFoundException;
import com.mgrigorakis.schedulo.infrastructure.storage.S3StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceTest {
    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private BusinessMemberRepository businessMemberRepository;

    @Mock
    private BusinessRoleRepository businessRoleRepository;

    @Mock
    private BusinessMapper businessMapper;

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private BusinessServiceImpl businessService;

    private static final String mockName = "ACME Barbers";
    private static final String mockEmail = "acme-barbers@example.com";
    private static final String mockCity = "Athens";
    private static final String mockBusinessLogoKey = "businesses/acme-barbers-athens/logo";
    private static final BusinessRole mockBusinessRole = new BusinessRole("OWNER", null);

    @Test
    void getBusinesses_shouldReturnAllBusinesses() {
        // Arrange
        List<Business> businesses = List.of(Business.builder().build(), Business.builder().build());
        when(businessRepository.findAll()).thenReturn(businesses);

        // Act
        List<BusinessResponse> response = businessService.getBusinesses();

        // Assert
        verify(businessRepository, times(1)).findAll();
        assertEquals(response.size(), businesses.size());
    }

    @Test
    void getBusinessById_shouldReturnBusiness_whenExists() {
        // Arrange
        Business business = Business.builder().email(mockEmail).build();
        BusinessResponse expectedResponse = new BusinessResponse(1L, mockName, "acme-barber", mockEmail, null, null,
                                                                 null, null, null, null, null, null, null);

        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));
        when(businessMapper.toResponse(business, null)).thenReturn(expectedResponse);

        // Act
        BusinessResponse response = businessService.getBusinessById(1L);

        // Assert
        assertEquals(response.email(), expectedResponse.email());
        verify(businessRepository, times(1)).findById(1L);
        verify(businessMapper, times(1)).toResponse(business, null);
    }

    @Test
    void getBusinessById_shouldThrowResourceNotFoundException_whenBusinessDoesNotExist() {
        // Arrange
        when(businessRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> businessService.getBusinessById(1L));
        verify(businessRepository, times(1)).findById(1L);
        verify(businessMapper, never()).toResponse(any(), any());
    }

    @Test
    void createBusiness_shouldCreateBusiness() {
        // Arrange
        Business business = Business.builder().name(mockName).city(mockCity).build();
        BusinessRequest request = buildBusinessRequest();

        when(businessMapper.toBusiness(request)).thenReturn(business);
        when(businessRepository.findBusinessBySlug(anyString())).thenReturn(Optional.empty());
        when(businessRoleRepository.findByName("OWNER")).thenReturn(Optional.of(mockBusinessRole));

        // Act
        businessService.createBusiness(request);

        // Assert
        ArgumentCaptor<Business> captor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository, times(1)).save(captor.capture());

        Business savedBusiness =  captor.getValue();
        assertEquals("acme-barbers-athens", savedBusiness.getSlug());
        verify(businessRoleRepository, times(1)).findByName("OWNER");
        verify(s3StorageService, never()).upload(anyString(), any(), any());
    }

    @Test
    void createBusiness_shouldGenerateDifferentSlug_whenBusinessAlreadyExistsBySlug() {
        // Arrange
        Business business = Business.builder().name(mockName).city(mockCity).build();
        BusinessRequest request = buildBusinessRequest();

        when(businessMapper.toBusiness(request)).thenReturn(business);
        when(businessRepository.findBusinessBySlug("acme-barbers-athens")).thenReturn(Optional.of(Business.builder().build()));
        when(businessRoleRepository.findByName("OWNER")).thenReturn(Optional.of(mockBusinessRole));

        // Act
        businessService.createBusiness(request);

        // Assert
        ArgumentCaptor<Business> captor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository, times(1)).save(captor.capture());

        Business savedBusiness =  captor.getValue();
        assertNotEquals("acme-barbers-athens", savedBusiness.getSlug());
        verify(businessRoleRepository, times(1)).findByName("OWNER");
        verify(s3StorageService, never()).upload(anyString(), any(), any());
    }

    @Test
    void createBusiness_shouldThrowResourceNotFoundException_whenBusinessOwnerBusinessRoleDoesNotExist() {
        // Arrange
        Business business = Business.builder().name(mockName).city(mockCity).build();
        BusinessRequest request = buildBusinessRequest();

        when(businessMapper.toBusiness(request)).thenReturn(business);
        when(businessRepository.findBusinessBySlug("acme-barbers-athens")).thenReturn(Optional.empty());
        when(businessRoleRepository.findByName("OWNER")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> businessService.createBusiness(request));

        verify(businessRepository, times(1)).save(business);
        verify(businessRoleRepository, times(1)).findByName("OWNER");
        verify(s3StorageService, never()).upload(anyString(), any(), any());
    }

    @Test
    void updateBusiness_shouldUpdateBusiness() {
        // Arrange
        Business business = Business.builder().name(mockName).build();
        Business updatedBusiness = Business.builder().name(mockName).email(mockEmail).build();
        BusinessRequest request = buildBusinessRequest();

        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));
        when(businessMapper.toUpdate(business, request)).thenReturn(updatedBusiness);

        // Act
        businessService.updateBusiness(1L, request);

        // Assert
        ArgumentCaptor<Business> captor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository, times(1)).save(captor.capture());

        Business savedBusiness =  captor.getValue();
        assertEquals(mockName, savedBusiness.getName());
        assertEquals(mockEmail, savedBusiness.getEmail());

        verify(businessRepository, times(1)).findById(1L);
    }

    @Test
    void updateBusiness_shouldThrowResourceNotFoundException_whenBusinessDoesNotExist() {
        // Arrange
        BusinessRequest request = buildBusinessRequest();

        when(businessRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> businessService.updateBusiness(1L, request));

        verify(businessRepository, times(1)).findById(1L);
        verify(businessRepository, never()).save(any(Business.class));
    }

    @Test
    void deleteBusiness_shouldDeleteBusiness() {
        // Arrange
        Business business = Business.builder().build();
        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));

        // Act
        businessService.deleteBusinessById(1L);

        // Arrange
        verify(businessRepository, times(1)).findById(1L);
        verify(businessRepository, times(1)).delete(business);
    }

    @Test
    void deleteBusiness_shouldThrowResourceNotFoundException_whenBusinessDoesNotExist() {
        // Arrange
        when(businessRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> businessService.deleteBusinessById(1L));

        verify(businessRepository, times(1)).findById(1L);
        verify(s3StorageService, never()).delete(anyString());
        verify(businessRepository, never()).delete(any(Business.class));
    }

    @Test
    void deleteBusiness_shouldDeleteAssociatedLogo() {
        // Arrange
        Business business = Business.builder().logoKey(mockBusinessLogoKey).build();
        when(businessRepository.findById(1L)).thenReturn(Optional.of(business));

        // Act
        businessService.deleteBusinessById(1L);

        // Assert
        verify(businessRepository, times(1)).findById(1L);
        verify(s3StorageService, times(1)).delete(mockBusinessLogoKey);
        verify(businessRepository, times(1)).delete(business);
    }

    private BusinessRequest buildBusinessRequest() {
        return new BusinessRequest(mockName, mockEmail, null, null, null, null, null, mockCity, null);
    }
}
