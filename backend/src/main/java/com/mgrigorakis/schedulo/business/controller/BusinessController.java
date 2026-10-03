package com.mgrigorakis.schedulo.business.controller;

import com.mgrigorakis.schedulo.business.dto.BusinessRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessResponse;
import com.mgrigorakis.schedulo.business.service.BusinessService;
import com.mgrigorakis.schedulo.common.dto.ApiResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/businesses")
@RestController
public class BusinessController {
    private final BusinessService businessService;

    @GetMapping
    public ApiResponseWrapper<List<BusinessResponse>> getBusiness() {
        return new ApiResponseWrapper<>(businessService.getBusinesses());
    }

    @GetMapping("/{id}")
    public ApiResponseWrapper<BusinessResponse> getBusinessById(@PathVariable Long id) {
        return new ApiResponseWrapper<>(businessService.getBusinessById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponseWrapper<BusinessResponse> createBusiness(@ModelAttribute @Valid BusinessRequest request) {
        return new ApiResponseWrapper<>(businessService.createBusiness(request));
    }

    @PutMapping("/{id}")
    public ApiResponseWrapper<BusinessResponse> updateBusinessById(@PathVariable Long id,
                                                                   @ModelAttribute @Valid BusinessRequest request) {
        return new ApiResponseWrapper<>(businessService.updateBusiness(id, request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBusinessById(@PathVariable Long id) {
        businessService.deleteBusinessById(id);
    }
}
