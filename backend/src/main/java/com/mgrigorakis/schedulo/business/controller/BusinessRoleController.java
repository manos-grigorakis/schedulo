package com.mgrigorakis.schedulo.business.controller;

import com.mgrigorakis.schedulo.business.dto.BusinessRoleRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessRoleResponse;
import com.mgrigorakis.schedulo.business.service.BusinessRoleService;
import com.mgrigorakis.schedulo.common.dto.ApiResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/business-roles")
@RestController
public class BusinessRoleController {
    private final BusinessRoleService businessRoleService;

    @GetMapping
    public ApiResponseWrapper<List<BusinessRoleResponse>> getAllBusinessRoles() {
        return new ApiResponseWrapper<>(businessRoleService.getAllBusinessRoles());
    }

    @GetMapping("/{id}")
    public ApiResponseWrapper<BusinessRoleResponse> getBusinessRoleById(@PathVariable Long id) {
        return new ApiResponseWrapper<>(businessRoleService.getBusinessRoleById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponseWrapper<BusinessRoleResponse> createBusinessRole(@RequestBody @Valid BusinessRoleRequest request) {
        return new ApiResponseWrapper<>(businessRoleService.createBusinessRole(request));
    }

    @PutMapping("/{id}")
    public ApiResponseWrapper<BusinessRoleResponse> updateBusinessRoleById(@PathVariable Long id,
                                                                           @RequestBody @Valid BusinessRoleRequest request) {
        return new ApiResponseWrapper<>(businessRoleService.updateBusinessRoleById(id, request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBusinessRoleById(@PathVariable Long id) {
        businessRoleService.deleteBusinessRoleById(id);
    }
}
