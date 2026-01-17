package com.progfilthi.awss3.dtos;

public record PresignedUrlResponse(
        String uploadUrl,
        String s3Key,
        Long expiresIn
) {
}
