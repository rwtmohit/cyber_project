package com.example.cyber.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UrlRequest {
    @NotBlank
    private String url;
}