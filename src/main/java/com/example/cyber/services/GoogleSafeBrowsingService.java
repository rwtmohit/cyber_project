package com.example.cyber.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class GoogleSafeBrowsingService {
        private final WebClient webClient;
        private final String apiKey;

        public GoogleSafeBrowsingService(WebClient.Builder builder,
                                                                         @Value("${custom.google.safebrowsing.api.key}") String apiKey) {
                this.webClient = builder.build();
                this.apiKey = apiKey;
        }

        public String checkUrl(String url) {
                String endpoint = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + apiKey;

                Map<String, Object> body = Map.of(
                                "client", Map.of(
                                                "clientId", "spring-boot-phishing-checker",
                                                "clientVersion", "1.0"
                                ),
                                "threatInfo", Map.of(
                                                "threatTypes", List.of("MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE"),
                                                "platformTypes", List.of("ANY_PLATFORM"),
                                                "threatEntryTypes", List.of("URL"),
                                                "threatEntries", List.of(Map.of("url", url))
                                )
                );

                try {
                        Map response = webClient.post()
                                        .uri(endpoint)
                                        .bodyValue(body)
                                        .retrieve()
                                        .bodyToMono(Map.class)
                                        .block();

                        if (response == null || !response.containsKey("matches")) {
                                return "SAFE";
                        }
                        return "UNSAFE";
                } catch (WebClientResponseException e) {
                        return "UNKNOWN: " + e.getStatusCode();
                } catch (Exception e) {
                        return "UNKNOWN";
                }
        }
}
