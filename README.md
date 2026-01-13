# 🚀 AWS S3 Spring Boot Backend Setup

## Overview

This document provides a complete guide to setting up a secure Spring Boot backend connected to AWS S3 for file uploads. The application is configured to handle large audio files with proper security practices.

---

## 1. Environment Variables (IntelliJ Configuration)

To keep secrets out of the code, store these variables in IntelliJ's **Run/Debug Configurations** under **Environment Variables**:

- `AWS_S3_ACCESS_KEY`: Your AWS Access Key
- `AWS_S3_SECRET_KEY`: Your AWS Secret Key

---

## 2. Spring Configuration (`application.yml`)

The configuration maps environment variables to Spring properties and increases file upload limits to support high-quality audio files.

```yaml
spring:
  application:
    name: aws-s3
  servlet:
    multipart:
      max-file-size: 50MB          # Supports large audio/beat files
      max-request-size: 50MB
      enabled: true

aws:
  s3:
    bucket-name: filthi-bucket
    region: ap-southeast-2
    access-key: ${AWS_S3_ACCESS_KEY}
    secret-key: ${AWS_S3_SECRET_KEY}
```

---

## 3. AWS S3 Configuration Bean (`S3Config.java`)

This class initializes the `S3Client` using the credentials provided via environment variables.

```java
@Configuration
public class S3Config {
    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}
```

---

## 4. S3 Service Logic (`S3Service.java`)

The service handles file uploads by sending the byte stream to your S3 bucket.

```java
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadToS3(MultipartFile file) throws IOException {
        String s3Key = "uploads/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return s3Key;
    }
}
```

---

## ✅ Summary of Fixed Issues

- **Security**: Identified and rotated compromised keys that were accidentally exposed
- **Circular Reference**: Fixed `application.yml` which was incorrectly referencing itself with `${aws.s3.access-key}`
- **Configuration Wiring**: Moved secrets to IntelliJ Environment Variables for runtime injection
- **Payload Limits**: Resolved `413 Too Large` error by adding the `MB` suffix to multipart settings

---

## 🌅 Tomorrow's Roadmap

### 1. Presigned URLs
Modify the service to return temporary URLs so users can access uploaded beats without making the S3 bucket public.

### 2. Frontend Integration
Set up a simple React or HTML form to allow users to select and upload files directly from their browser instead of using Postman.

---

## 📝 Notes

This setup provides a secure, production-ready foundation for handling file uploads to AWS S3. All sensitive credentials are properly externalized and the application is configured to handle large media files efficiently.
