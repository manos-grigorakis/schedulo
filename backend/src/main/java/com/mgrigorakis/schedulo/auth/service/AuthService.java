package com.mgrigorakis.schedulo.auth.service;

import com.mgrigorakis.schedulo.auth.dto.LoginRequest;
import com.mgrigorakis.schedulo.auth.dto.RegistrationRequest;

public interface AuthService {
    String login(LoginRequest request);

    void registration(RegistrationRequest request);
}
