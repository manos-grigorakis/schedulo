package com.mgrigorakis.schedulo.business.service;

import com.mgrigorakis.schedulo.business.dto.BusinessRoleRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessRoleResponse;

import java.util.List;

public interface BusinessRoleService {
    List<BusinessRoleResponse> getAllBusinessRoles();

    BusinessRoleResponse getBusinessRoleById(Long id);

    BusinessRoleResponse createBusinessRole(BusinessRoleRequest request);

    BusinessRoleResponse updateBusinessRoleById(Long id, BusinessRoleRequest request);

    void deleteBusinessRoleById(Long id);
}
