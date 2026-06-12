package com.example.cyber.services;

import com.example.cyber.dtos.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class VirusTotalService {

    private final WebClient webClient;
    private final String apiKey;

    public VirusTotalService(
            WebClient.Builder builder,
            @Value("${custom.security.virustotal.api-key}") String apiKey) {

        this.webClient = builder.build();
        this.apiKey = apiKey;
    }

    public UrlScanResponse checkUrl(String url) {

        String normalizedUrl = normalizeUrl(url);

        if (normalizedUrl == null) {
            throw new RuntimeException("Invalid URL");
        }

        try {

            String urlId = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(
                            normalizedUrl.getBytes(StandardCharsets.UTF_8)
                    );

            Map response = webClient.get()
                    .uri("https://www.virustotal.com/api/v3/urls/" + urlId)
                    .header("x-apikey", apiKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            Map data =
                    (Map) response.get("data");

            Map attributes =
                    (Map) data.get("attributes");

            Map stats =
                    (Map) attributes.get("last_analysis_stats");

            int malicious =
                    ((Number) stats.getOrDefault("malicious", 0))
                            .intValue();

            int suspicious =
                    ((Number) stats.getOrDefault("suspicious", 0))
                            .intValue();

            int harmless =
                    ((Number) stats.getOrDefault("harmless", 0))
                            .intValue();

            int undetected =
                    ((Number) stats.getOrDefault("undetected", 0))
                            .intValue();

            int total =
                    malicious +
                    suspicious +
                    harmless +
                    undetected;

            double riskPercentage =
                    total == 0
                            ? 0
                            : ((double) malicious / total) * 100;

            String status;

            if (malicious > 0) {
                status = "MALICIOUS";
            } else if (suspicious > 0) {
                status = "SUSPICIOUS";
            } else {
                status = "CLEAN";
            }

            List<String> flaggedBy = new ArrayList<>();

            Object resultsObj =
                    attributes.get("last_analysis_results");

            if (resultsObj instanceof Map) {

                Map<String, Object> results =
                        (Map<String, Object>) resultsObj;

                for (Map.Entry<String, Object> entry : results.entrySet()) {

                    if (!(entry.getValue() instanceof Map)) {
                        continue;
                    }

                    Map vendor =
                            (Map) entry.getValue();

                    String category =
                            String.valueOf(
                                    vendor.getOrDefault(
                                            "category",
                                            ""
                                    )
                            );

                    if ("malicious".equalsIgnoreCase(category)
                            || "phishing".equalsIgnoreCase(category)
                            || "suspicious".equalsIgnoreCase(category)) {

                        flaggedBy.add(entry.getKey());
                    }
                }
            }

            return new UrlScanResponse(
                    normalizedUrl,
                    status,
                    malicious,
                    suspicious,
                    harmless,
                    undetected,
                    total,
                    Math.round(riskPercentage * 100.0) / 100.0,
                    flaggedBy
            );

        } catch (WebClientResponseException e) {

            throw new RuntimeException(
                    e.getStatusCode()
                            + " : "
                            + e.getResponseBodyAsString()
            );

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    private String normalizeUrl(String url) {

        try {

            if (url == null || url.isBlank()) {
                return null;
            }

            String normalized = url.trim();

            if (!normalized.matches("^(?i)https?://.*")) {
                normalized = "https://" + normalized;
            }

            return new URL(normalized).toString();

        } catch (Exception e) {
            return null;
        }
    }
}