package com.example.cyber.services;

import com.example.cyber.dtos.*;
import org.springframework.stereotype.Service;
@Service
public class UrlVerdictService {
private final GoogleSafeBrowsingService safeBrowsingService;
    private final VirusTotalService virusTotalService;

    public UrlVerdictService(GoogleSafeBrowsingService safeBrowsingService,
                             VirusTotalService virusTotalService) {
        this.safeBrowsingService = safeBrowsingService;
        this.virusTotalService = virusTotalService;
    }

    public UrlCheckResponse check(String url) {
        String googleStatus = safeBrowsingService.checkUrl(url);
        String vtStatus = virusTotalService.checkUrl(url);

        String verdict;
        String details;

        if ("UNSAFE".equals(googleStatus)) {
            verdict = "KNOWN_PHISHING_OR_MALICIOUS";
            details = "Google Safe Browsing flagged this URL as unsafe.";
        } else if ("MALICIOUS".equals(vtStatus)) {
            verdict = "BLACKLISTED";
            details = "VirusTotal shows malicious detection.";
        } else if ("SUSPICIOUS".equals(vtStatus)) {
            verdict = "SUSPICIOUS";
            details = "VirusTotal shows suspicious detection.";
        } else if ("CLEAN".equals(vtStatus) && "SAFE".equals(googleStatus)) {
            verdict = "CLEAN";
            details = "No major threat detected by either service.";
        } else {
            verdict = "UNKNOWN";
            details = "Insufficient data from one or both services.";
        }

        return new UrlCheckResponse(url, verdict, googleStatus, vtStatus, details);
    }
}
