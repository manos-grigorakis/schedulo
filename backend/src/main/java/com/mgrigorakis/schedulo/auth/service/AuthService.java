package com.mgrigorakis.schedulo.auth.service;

import com.mgrigorakis.schedulo.auth.dto.LoginRequest;
import com.mgrigorakis.schedulo.auth.dto.LoginResponse;
import com.mgrigorakis.schedulo.auth.dto.RegistrationRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    void registration(RegistrationRequest request);
}
