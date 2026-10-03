package com.mgrigorakis.schedulo.infrastructure.storage;

import com.mgrigorakis.schedulo.common.exception.StorageServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3StorageServiceImpl implements S3StorageService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${app.aws.s3.bucket-name}")
    private String bucketName;


    @Override
    public void upload(String key, byte[] content, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
            log.info("Uploaded file to s3://" + bucketName + "/" + key);
        } catch (S3Exception e) {
            log.error("Error uploading file: {} to S3", key, e);
            throw new StorageServiceException("Storage service error");
        } catch (SdkException e) {
            log.error("SDK client error while uploading file: {} to S3", key, e);
            throw new StorageServiceException("Storage client error");
        } catch (Exception e) {
            log.error("Error while uploading file: {} to S3", key, e);
            throw new StorageServiceException("Error while uploading file");
        }
    }

    @Override
    public String generatePresignedUrl(String key) {
        GetObjectRequest objectAclRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(objectAclRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedGetObjectRequest.url().toExternalForm();
    }

    @Override
    public void delete(String key) {
        s3Client.deleteObject(request -> request.bucket(bucketName).key(key));
    }
}
