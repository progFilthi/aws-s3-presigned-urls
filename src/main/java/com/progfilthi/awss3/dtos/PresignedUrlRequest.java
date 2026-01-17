package com.progfilthi.awss3.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PresignedUrlRequest(

        @NotBlank(message = "File name is required.")
//        @Pattern(regexp = ".*\\.wav$", message = "File name must end with .wav")
        String fileName,

        @NotBlank(message = "Content type is required.")
        @Pattern(regexp = "^audio/(wav|x-wav|vnd\\.wave)$", message = "Only WAV audio files are allowed.")
        String contentType
) {
}
