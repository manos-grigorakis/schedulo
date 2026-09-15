package com.mgrigorakis.schedulo.business.service;

import com.mgrigorakis.schedulo.business.dto.BusinessRequest;
import com.mgrigorakis.schedulo.business.dto.BusinessResponse;
import com.mgrigorakis.schedulo.business.mapper.BusinessMapper;
import com.mgrigorakis.schedulo.business.model.Business;
import com.mgrigorakis.schedulo.business.repository.BusinessRepository;
import com.mgrigorakis.schedulo.common.exception.BadRequestException;
import com.mgrigorakis.schedulo.common.exception.ResourceNotFoundException;
import com.mgrigorakis.schedulo.infrastructure.storage.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final S3StorageService s3StorageService;

    private static final List<String> ALLOWED_LOGO_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/svg+xml");

    @Value("${app.aws.s3.bucket-path-businesses}")
    private String s3BucketPathBusinesses;

    @Override
    public List<BusinessResponse> getBusinesses() {
        return businessRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public BusinessResponse getBusinessById(Long id) {
        Business business = businessRepository.findById(id).orElseThrow(() -> {
            log.warn("Business not found with id {}", id);
            return new ResourceNotFoundException("Business not found with id " + id);
        });

        return toResponse(business);
    }

    @Override
    public BusinessResponse createBusiness(BusinessRequest request) {
        Business business = businessMapper.toBusiness(request);
        String slug = business.buildSlug();

        if (businessRepository.findBusinessBySlug(slug).isPresent()) {
            slug += "-" + UUID.randomUUID().toString().substring(0, 6);
        }

        business.setSlug(slug);
        uploadLogoFile(request.logo(), business);

        businessRepository.save(business);
        log.info("Business created with id {}", business.getId());

        return toResponse(business);
    }

    @Override
    public BusinessResponse updateBusiness(Long id, BusinessRequest request) {
        Business business = businessRepository.findById(id).orElseThrow(() -> {
            log.warn("Business not found with id {}", id);
            return new ResourceNotFoundException("Business not found with id " + id);
        });

        Business updatedBusiness = businessMapper.toUpdate(business, request);
        uploadLogoFile(request.logo(), updatedBusiness);

        businessRepository.save(updatedBusiness);
        log.info("Business updated with id {}", updatedBusiness.getId());

        return toResponse(updatedBusiness);
    }

    @Override
    public void deleteBusinessById(Long id) {
        Business business = businessRepository.findById(id).orElseThrow(() -> {
            log.warn("Business not found with id {}", id);
            return new ResourceNotFoundException("Business not found with id " + id);
        });

        if (business.getLogoKey() != null) {
            s3StorageService.delete(business.getLogoKey());
        }

        businessRepository.delete(business);
        log.info("Business deleted with id {}", id);
    }

    /**
     * Uploads the provided {@link MultipartFile} on S3 object storage and assigns the generated object key to the
     * business
     *
     * @param logoFile The Logo file to upload to S3 object storage
     * @param business The business associated with the logo
     */
    private void uploadLogoFile(MultipartFile logoFile, Business business) {
        if (logoFile != null && !logoFile.isEmpty()) {
            validateLogoFile(logoFile);

            String logoKey = s3BucketPathBusinesses + business.getSlug() + "/logo";

            try {
                s3StorageService.upload(logoKey, logoFile.getBytes(), logoFile.getContentType());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            business.setLogoKey(logoKey);
        }
    }

    /**
     * Validates a {@link MultipartFile} based on the allowed content types defined in
     * {@link #ALLOWED_LOGO_CONTENT_TYPES}
     *
     * @param logoFile The Logo file to validate
     */
    private void validateLogoFile(MultipartFile logoFile) {
        String contentType = logoFile.getContentType();

        if (!ALLOWED_LOGO_CONTENT_TYPES.contains(contentType)) {
            throw new BadRequestException("Unsupported logo content type: " + contentType, "UNSUPORTED_CONTENT_TYPE");
        }
    }

    /**
     * Maps a business to its response, including a presigned URL for the logo when it's available
     *
     * @param business The business to map
     * @return The mapped business response
     */
    private BusinessResponse toResponse(Business business) {
        String logoUrl = business.getLogoKey() != null
                ? s3StorageService.generatePresignedUrl(business.getLogoKey()) : null;

        return businessMapper.toResponse(business, logoUrl);
    }
}

