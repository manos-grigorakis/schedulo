package com.mgrigorakis.schedulo.business.mapper;

import com.mgrigorakis.schedulo.business.dto.BusinessRoleRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessRoleResponse;
import com.mgrigorakis.schedulo.business.model.BusinessRole;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BusinessRoleMapper {
    BusinessRole toEntity(BusinessRoleRequest request);

    BusinessRoleResponse toResponse(BusinessRole businessRole);

    BusinessRole toUpdate(@MappingTarget BusinessRole businessRole, BusinessRoleRequest request);
}
