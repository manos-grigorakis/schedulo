package com.mgrigorakis.schedulo.auth.controller;

import com.mgrigorakis.schedulo.auth.dto.LoginRequest;
import com.mgrigorakis.schedulo.auth.dto.LoginResponse;
import com.mgrigorakis.schedulo.auth.dto.RegistrationRequest;
import com.mgrigorakis.schedulo.auth.service.AuthService;
import com.mgrigorakis.schedulo.common.dto.ApiResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponseWrapper<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return new ApiResponseWrapper<>(authService.login(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public void registration(@RequestBody @Valid RegistrationRequest request) {
        authService.registration(request);
    }
}
