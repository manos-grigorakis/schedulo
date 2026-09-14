package com.mgrigorakis.schedulo.auth.mapper;

import com.mgrigorakis.schedulo.auth.dto.RegistrationRequest;
import com.mgrigorakis.schedulo.users.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "password", source = "hashedPassword")
    User toUserFromRegistration(RegistrationRequest registrationRequest, String hashedPassword);

    @Mapping(target = "firstName", source = "givenName")
    @Mapping(target = "lastName", source = "familyName")
    @Mapping(target = "email", source = "email")
    User toUserFromOidcUser(OidcUser oidcUser);
}
