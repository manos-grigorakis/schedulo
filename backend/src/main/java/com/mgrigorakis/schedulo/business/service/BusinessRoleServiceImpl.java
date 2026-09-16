package com.mgrigorakis.schedulo.business.service;

import com.mgrigorakis.schedulo.business.dto.BusinessRoleRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessRoleResponse;
import com.mgrigorakis.schedulo.business.mapper.BusinessRoleMapper;
import com.mgrigorakis.schedulo.business.model.BusinessRole;
import com.mgrigorakis.schedulo.business.repository.BusinessRoleRepository;
import com.mgrigorakis.schedulo.common.exception.DuplicateEntryException;
import com.mgrigorakis.schedulo.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BusinessRoleServiceImpl implements BusinessRoleService {
    private final BusinessRoleRepository businessRoleRepository;
    private final BusinessRoleMapper businessRoleMapper;

    @Override
    public List<BusinessRoleResponse> getAllBusinessRoles() {
        List<BusinessRole> businessRoles = businessRoleRepository.findAll();

        return businessRoles.stream().map(businessRoleMapper::toResponse).toList();
    }

    @Override
    public BusinessRoleResponse getBusinessRoleById(Long id) {
        BusinessRole businessRole = businessRoleRepository.findById(id).orElseThrow(() -> {
            log.warn("BusinessRole with id {} not found", id);
            return new ResourceNotFoundException("BusinessRole with id " + id + " not found");
        });

        return businessRoleMapper.toResponse(businessRole);
    }

    @Override
    public BusinessRoleResponse createBusinessRole(BusinessRoleRequest request) {
        if (businessRoleRepository.existsByName(request.name())) {
            log.warn("BusinessRole with name {} already exists", request.name());
            throw new DuplicateEntryException("BusinessRole with name " + request.name() + " already exists");
        }

        BusinessRole businessRole = businessRoleMapper.toEntity(request);
        businessRoleRepository.save(businessRole);
        log.info("BusinessRole with name {} created", request.name());

        return businessRoleMapper.toResponse(businessRole);
    }

    @Override
    public BusinessRoleResponse updateBusinessRoleById(Long id, BusinessRoleRequest request) {
        BusinessRole businessRole = businessRoleRepository.findById(id).orElseThrow(() -> {
            log.warn("BusinessRole with id {} not found", id);
            return new ResourceNotFoundException("BusinessRole with id " + id + " not found");
        });

        if(businessRoleRepository.existsByNameAndIdNot(request.name(), id)) {
            log.warn("BusinessRole with name {} already exists", request.name());
            throw new DuplicateEntryException("BusinessRole with name " + request.name() + " already exists");
        }

        BusinessRole updatedBusinessRole = businessRoleMapper.toUpdate(businessRole, request);
        businessRoleRepository.save(updatedBusinessRole);
        log.info("BusinessRole with id {} updated", id);

        return businessRoleMapper.toResponse(updatedBusinessRole);
    }

    @Override
    public void deleteBusinessRoleById(Long id) {
        BusinessRole businessRole = businessRoleRepository.findById(id).orElseThrow(() -> {
            log.warn("BusinessRole with id {} not found", id);
            return new ResourceNotFoundException("BusinessRole with id " + id + " not found");
        });

        businessRoleRepository.delete(businessRole);
        log.info("BusinessRole with id {} has been deleted", id);
    }
}
