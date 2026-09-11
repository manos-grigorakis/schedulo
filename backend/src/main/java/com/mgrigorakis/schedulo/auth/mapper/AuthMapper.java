package com.mgrigorakis.schedulo.auth.mapper;

import com.mgrigorakis.schedulo.auth.dto.RegistrationRequest;
import com.mgrigorakis.schedulo.users.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "password", source = "hashedPassword")
    User toUserFromRegistration(RegistrationRequest registrationRequest, String hashedPassword);
}
