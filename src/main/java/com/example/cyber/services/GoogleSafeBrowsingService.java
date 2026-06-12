package com.example.cyber.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GoogleSafeBrowsingService {

    private final WebClient webClient = WebClient.create();

    @Value("${custom.google.safebrowsing.api.key}")
    private String apiKey;

    public String checkUrl(String url) {

        String endpoint =
                "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "client", Map.of(
                        "clientId", "spring-app",
                        "clientVersion", "1.0"
                ),
                "threatInfo", Map.of(
                        "threatTypes", List.of(
                                "MALWARE",
                                "SOCIAL_ENGINEERING",
                                "UNWANTED_SOFTWARE"
                        ),
                        "platformTypes", List.of("ANY_PLATFORM"),
                        "threatEntryTypes", List.of("URL"),
                        "threatEntries", List.of(
                                Map.of("url", url)
                        )
                )
        );

        try {

            Map<?, ?> response = webClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("matches")) {
                return "SAFE";
            }

            return "UNSAFE";

        } catch (Exception e) {
            return "ERROR : " + e.getMessage();
        }
    }
}