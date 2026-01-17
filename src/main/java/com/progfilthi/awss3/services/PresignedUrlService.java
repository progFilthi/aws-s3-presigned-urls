package com.progfilthi.awss3.services;

import com.progfilthi.awss3.dtos.PresignedUrlRequest;
import com.progfilthi.awss3.dtos.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {

    private final S3Presigner s3Presigner;

    private final static Duration URL_EXPIRATION = Duration.ofMinutes(10);

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    public PresignedUrlResponse generatePresignedUploadUrl(PresignedUrlRequest request){

        //todo: generate a unique key
        String s3Key = generateS3Key(request.fileName());

        //todo: initiate a put object request
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(request.contentType())
                .build();

        //todo: Presign the request with expiration
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(URL_EXPIRATION)
                .putObjectRequest(putObjectRequest)
                .build();

        //todo: Generate the presigned url
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return new PresignedUrlResponse(
                presignedRequest.url().toString(),
                s3Key,
                URL_EXPIRATION.toSeconds()
        );
    }


    private String generateS3Key(String fileName){
        LocalDate today = LocalDate.now();
        String datePath = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return "uploads/" + datePath + "/" + uniqueId + "-" + fileName;
    }
}
