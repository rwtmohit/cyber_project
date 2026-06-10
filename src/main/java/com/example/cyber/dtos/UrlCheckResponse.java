package com.example.cyber.dtos;

public record UrlCheckResponse(
        String url,
        String verdict,
        String googleStatus,
        String virusTotalStatus,
        String details
) {}
