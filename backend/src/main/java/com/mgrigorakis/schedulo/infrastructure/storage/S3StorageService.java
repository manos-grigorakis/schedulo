package com.mgrigorakis.schedulo.infrastructure.storage;

public interface S3StorageService {
    void upload(String key, byte[] content, String contentType);

    String generatePresignedUrl(String key);

    void delete(String key);
}
