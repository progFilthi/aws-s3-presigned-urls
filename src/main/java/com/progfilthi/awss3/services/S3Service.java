package com.progfilthi.awss3.services;

import com.progfilthi.awss3.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    public String bucketName;


    public String uploadToS3(MultipartFile file) throws IOException {

        String s3Key = "uploads/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();


        PutObjectRequest putOj = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        s3Client.putObject(putOj, RequestBody.fromBytes(file.getBytes()));

        return s3Key;
    }
}
