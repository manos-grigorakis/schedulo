package com.mgrigorakis.schedulo.business.mapper;

import com.mgrigorakis.schedulo.business.dto.BusinessRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessResponse;
import com.mgrigorakis.schedulo.business.model.Business;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    Business toBusiness(BusinessRequest businessRequest);

    @Mapping(target = "logoUrl", source = "logoUrl")
    BusinessResponse toResponse(Business business, String logoUrl);

    Business toUpdate(@MappingTarget Business business,  BusinessRequest businessRequest);
}
