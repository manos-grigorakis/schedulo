package com.mgrigorakis.schedulo.config;

import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3ClientConfig {
    @Value("${app.aws.s3.access-key}")
    private String accessKey;

    @Value("${app.aws.s3.secret-key}")
    private String secretKey;

    @Value("${app.aws.s3.internal-endpoint}")
    private String internalEndpoint;

    @Value("${app.aws.s3.public-endpoint}")
    private String publicEndpoint;

    @Value("${app.aws.s3.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        Region s3Region = Region.of(region);

        return S3Client.builder()
                .endpointOverride(URI.create(internalEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(s3Region)
                // Required for MinIO
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        Region s3Region = Region.of(region);

        return S3Presigner.builder()
                .endpointOverride(URI.create(publicEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(s3Region)
                // Required for MinIO
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }
}
