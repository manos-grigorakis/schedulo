package com.mgrigorakis.schedulo.business.service;

import com.mgrigorakis.schedulo.business.dto.BusinessRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessResponse;

import java.util.List;

public interface BusinessService {
    List<BusinessResponse> getBusinesses();

    BusinessResponse getBusinessById(Long id);

    BusinessResponse createBusiness(BusinessRequest request);

    BusinessResponse updateBusiness(Long id, BusinessRequest request);

    void deleteBusinessById(Long id);
}
