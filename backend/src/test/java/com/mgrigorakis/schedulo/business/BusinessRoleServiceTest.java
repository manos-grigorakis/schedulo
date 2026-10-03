package com.mgrigorakis.schedulo.business;

import com.mgrigorakis.schedulo.business.dto.BusinessRoleRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessRoleResponse;
import com.mgrigorakis.schedulo.business.mapper.BusinessRoleMapper;
import com.mgrigorakis.schedulo.business.model.BusinessRole;
import com.mgrigorakis.schedulo.business.repository.BusinessRoleRepository;
import com.mgrigorakis.schedulo.business.service.BusinessRoleServiceImpl;
import com.mgrigorakis.schedulo.common.exception.DuplicateEntryException;
import com.mgrigorakis.schedulo.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusinessRoleServiceTest {
    @Mock
    private BusinessRoleRepository businessRoleRepository;

    @Mock
    private BusinessRoleMapper businessRoleMapper;

    @InjectMocks
    private BusinessRoleServiceImpl businessRoleService;

    private static final BusinessRole mockBusinessRole = new BusinessRole("OWNER", null);
    private static final BusinessRoleRequest mockRequest = new BusinessRoleRequest("OWNER", null);

    @Test
    void getAllBusinessRoles_shouldReturnAllBusinessRoles() {
        // Arrange
        BusinessRole businessRole = new BusinessRole("MANAGER", null);
        List<BusinessRole> businessRoles = List.of(mockBusinessRole, businessRole);

        when(businessRoleRepository.findAll()).thenReturn(businessRoles);

        // Act
        List<BusinessRoleResponse> response = businessRoleService.getAllBusinessRoles();

        // Assert
        assertEquals(businessRoles.size(), response.size());
        verify(businessRoleRepository, times(1)).findAll();
    }

    @Test
    void getBusinessRoleById_shouldReturnBusinessRole_whenExists() {
        // Arrange
        when(businessRoleRepository.findById(1L)).thenReturn(Optional.of(mockBusinessRole));

        // Act
        businessRoleService.getBusinessRoleById(1L);

        // Assert
        verify(businessRoleRepository, times(1)).findById(1L);
        verify(businessRoleMapper, times(1)).toResponse(mockBusinessRole);
    }

    @Test
    void getBusinessRoleById_shouldThrowResourceNotFoundException_whenBusinessRoleNotFound() {
        // Arrange
        when(businessRoleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> businessRoleService.getBusinessRoleById(1L));

        verify(businessRoleRepository, times(1)).findById(1L);
        verify(businessRoleMapper, never()).toResponse(mockBusinessRole);
    }

    @Test
    void createBusinessRole_shouldCreateBusinessRole() {
        // Arrange
        when(businessRoleRepository.existsByName("OWNER")).thenReturn(false);
        when(businessRoleMapper.toEntity(mockRequest)).thenReturn(mockBusinessRole);

        // Act
        businessRoleService.createBusinessRole(mockRequest);

        // Assert
        verify(businessRoleRepository, times(1)).existsByName("OWNER");
        verify(businessRoleMapper, times(1)).toEntity(mockRequest);
        verify(businessRoleRepository, times(1)).save(mockBusinessRole);
        verify(businessRoleMapper, times(1)).toResponse(mockBusinessRole);
    }

    @Test
    void createBusinessRole_shouldThrowDuplicateEntryException_whenBusinessRoleAlreadyExists() {
        // Arrange
        when(businessRoleRepository.existsByName("OWNER")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEntryException.class, () ->  businessRoleService.createBusinessRole(mockRequest));

        verify(businessRoleRepository, times(1)).existsByName("OWNER");
        verify(businessRoleRepository, never()).save(any(BusinessRole.class));
    }

    @Test
    void updateBusinessRoleById_shouldUpdateBusinessRoleById() {
        // Arrange
        BusinessRole updatedBusinessRole = new BusinessRole("MANAGER", null);
        BusinessRoleRequest request = new BusinessRoleRequest("MANAGER", null);

        when(businessRoleRepository.findById(1L)).thenReturn(Optional.of(mockBusinessRole));
        when(businessRoleRepository.existsByNameAndIdNot("MANAGER", 1L)).thenReturn(false);
        when(businessRoleMapper.toUpdate(mockBusinessRole, request)).thenReturn(updatedBusinessRole);

        // Act
        businessRoleService.updateBusinessRoleById(1L, request);

        // Assert
        verify(businessRoleRepository, times(1)).findById(1L);
        verify(businessRoleRepository, times(1)).existsByNameAndIdNot("MANAGER", 1L);
        verify(businessRoleMapper, times(1)).toUpdate(mockBusinessRole, request);
        verify(businessRoleRepository, times(1)).save(updatedBusinessRole);
        verify(businessRoleMapper, times(1)).toResponse(updatedBusinessRole);
    }

    @Test
    void updateBusinessRoleById_shouldThrowResourceNotFoundException_whenBusinessRoleNotFound() {
        // Arrange
        when(businessRoleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                     () -> businessRoleService.updateBusinessRoleById(1L, mockRequest));

        verify(businessRoleRepository, times(1)).findById(1L);
        verify(businessRoleRepository, never()).save(any(BusinessRole.class));
        verify(businessRoleMapper, never()).toResponse(any());
    }

    @Test
    void updateBusinessRoleById_shouldThrowDuplicateEntryException_whenBusinessRoleAlreadyExists() {
        // Arrange
        when(businessRoleRepository.findById(1L)).thenReturn(Optional.of(mockBusinessRole));
        when(businessRoleRepository.existsByNameAndIdNot("OWNER", 1L)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEntryException.class, () -> businessRoleService.updateBusinessRoleById(1L, mockRequest));

        verify(businessRoleRepository, times(1)).findById(1L);
        verify(businessRoleRepository, times(1)).existsByNameAndIdNot("OWNER", 1L);
        verify(businessRoleRepository, never()).save(any(BusinessRole.class));
        verify(businessRoleMapper, never()).toResponse(any());
    }

    @Test
    void deleteBusinessRoleById_shouldDeleteBusinessRole() {
        // Arrange
        when(businessRoleRepository.findById(1L)).thenReturn(Optional.of(mockBusinessRole));

        // Act
        businessRoleService.deleteBusinessRoleById(1L);

        // Assert
        verify(businessRoleRepository, times(1)).findById(1L);
        verify(businessRoleRepository, times(1)).delete(mockBusinessRole);
    }

    @Test
    void deleteBusinessRoleById_shouldThrowResourceNotFoundException_whenBusinessRoleNotFound() {
        // Arrange
        when(businessRoleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> businessRoleService.deleteBusinessRoleById(1L));
        verify(businessRoleRepository, times(1)).findById(1L);
        verify(businessRoleRepository, never()).delete(any(BusinessRole.class));
    }
}
