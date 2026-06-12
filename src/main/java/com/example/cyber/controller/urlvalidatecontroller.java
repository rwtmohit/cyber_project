package com.example.cyber.controller;

import com.example.cyber.dtos.*;
import com.example.cyber.services.UrlVerdictService;

import com.example.cyber.services.GoogleSafeBrowsingService;
import com.example.cyber.services.VirusTotalService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/url")
public class urlvalidatecontroller {

    private final UrlVerdictService urlVerdictService;
    private final GoogleSafeBrowsingService googleSafeBrowsingService;
    private final VirusTotalService virusTotalService;

    public urlvalidatecontroller(UrlVerdictService urlVerdictService,
                                 GoogleSafeBrowsingService googleSafeBrowsingService,
                                 VirusTotalService virusTotalService) {
        this.urlVerdictService = urlVerdictService;
        this.googleSafeBrowsingService = googleSafeBrowsingService;
        this.virusTotalService = virusTotalService;
    }

    @PostMapping("/check")
    public ResponseEntity<UrlCheckResponse> checkUrl(@Valid @RequestBody UrlCheckRequest request) {
        UrlCheckResponse response = urlVerdictService.check(request.url());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/google-safeservice")
    public ResponseEntity<String> checkGoogleSafeService(@RequestParam("url") String url) {
        String result = googleSafeBrowsingService.checkUrl(url);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/virustotal")
    public ResponseEntity<UrlScanResponse> checkVirusTotal(@RequestParam("url") String url) {
        UrlScanResponse result = virusTotalService.checkUrl(url);
        return ResponseEntity.ok(result);
    }
}
