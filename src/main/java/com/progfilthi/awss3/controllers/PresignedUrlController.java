package com.progfilthi.awss3.controllers;

import com.progfilthi.awss3.dtos.PresignedUrlRequest;
import com.progfilthi.awss3.dtos.PresignedUrlResponse;
import com.progfilthi.awss3.services.PresignedUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class PresignedUrlController {

    private final PresignedUrlService  presignedUrlService;

    @PostMapping("presigned-upload-url")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUploadUrl(@Valid @RequestBody
                                                                           PresignedUrlRequest request){
        PresignedUrlResponse response = presignedUrlService.generatePresignedUploadUrl(request);
        return ResponseEntity.ok(response);
    }

}
