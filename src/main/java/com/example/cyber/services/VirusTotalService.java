package com.example.cyber.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Base64;
import java.util.Map;

@Service
public class VirusTotalService {
    private final WebClient webClient;
    private final String apiKey;

    public VirusTotalService(WebClient.Builder builder,
                             @Value("${custom.security.virustotal.api-key}") String apiKey) {
        this.webClient = builder.build();
        this.apiKey = apiKey;
    }

    // VirusTotal V3 requires submitting the URL to /api/v3/urls (POST) to get an id,
    // then GET /api/v3/urls/{id} to retrieve analysis attributes. The id is the
    // base64url (without padding) encoded URL.
    public String checkUrl(String url) {
        try {
            // Step 1: POST the url to /api/v3/urls to create/lookup
            Map postResp = webClient.post()
                    .uri("https://www.virustotal.com/api/v3/urls")
                    .header("x-apikey", apiKey)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromValue("url=" + url))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (postResp == null) return "UNKNOWN";
            Object dataObj = postResp.get("data");
            if (!(dataObj instanceof Map)) return "UNKNOWN";
            Map data = (Map) dataObj;
            Object idObj = data.get("id");
            if (!(idObj instanceof String)) return "UNKNOWN";
            String id = (String) idObj;

            // Step 2: GET the URL report
            Map getResp = webClient.get()
                    .uri("https://www.virustotal.com/api/v3/urls/" + id)
                    .header("x-apikey", apiKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (getResp == null) return "UNKNOWN";
            Object getDataObj = getResp.get("data");
            if (!(getDataObj instanceof Map)) return "UNKNOWN";
            Map getData = (Map) getDataObj;
            Object attrObj = getData.get("attributes");
            if (!(attrObj instanceof Map)) return "UNKNOWN";
            Map attributes = (Map) attrObj;
            Object statsObj = attributes.get("last_analysis_stats");
            if (!(statsObj instanceof Map)) return "UNKNOWN";
            Map stats = (Map) statsObj;

            int malicious = ((Number) stats.getOrDefault("malicious", 0)).intValue();
            int suspicious = ((Number) stats.getOrDefault("suspicious", 0)).intValue();

            if (malicious > 0) return "MALICIOUS";
            if (suspicious > 0) return "SUSPICIOUS";
            return "CLEAN";
        } catch (WebClientResponseException e) {
            return "UNKNOWN: " + e.getStatusCode();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
}
