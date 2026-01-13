package com.progfilthi.awss3.controllers;

import com.progfilthi.awss3.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;


    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@ModelAttribute MultipartFile file) {
        try{
            String s3Key = s3Service.uploadToS3(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully"+ s3Key);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }
}
