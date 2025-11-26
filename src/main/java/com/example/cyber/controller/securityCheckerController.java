package com.example.cyber.controller;

import com.example.cyber.services.urlanalysis;
import com.example.cyber.services.score;
import com.example.cyber.services.ssl;
import com.example.cyber.services.header;

import com.example.cyber.model.SSLReport;
import com.example.cyber.model.HeaderSecurityResult;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController                      // Return JSON
@RequestMapping("/api")               // API base path
public class securityCheckerController {

    @Autowired
    private urlanalysis urlAnalysisService;

    @Autowired
    private score scoreService;

    @Autowired
    private ssl sslService;

    @Autowired
    private header headerService;

    /**
     * Analyze website security and return results as JSON.
     *
     * Example:
     * GET /api/analyze?url=https://google.com
     */
    @GetMapping("/analyze")
    public Map<String, Object> analyzeWebsite(@RequestParam String url) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. URL analysis score
            int urlScore = urlAnalysisService.analyzeURLScore(url);

            // 2. SSL evaluation (0 or 40)
            SSLReport sslReport = sslService.checkSSL(url);
            int sslScore = (sslReport != null && sslReport.isValid()) ? 40 : 0;

            // 3. Header security score
            HeaderSecurityResult headerResult = headerService.checkSecurityHeaders(url);
            int headerScore = (headerResult != null) ? headerResult.score : 0;

            // 4. Calculate total score
            int totalScore = scoreService.calculateTotalScore(sslScore, headerScore, urlScore);

            // 5. Risk classification
            String riskLevel = scoreService.classifyRisk(totalScore);

            // Build JSON response
            response.put("url", url);
            response.put("urlPatternScore", urlScore);
            response.put("sslScore", sslScore);
            response.put("headerScore", headerScore);
            response.put("totalScore", totalScore);
            response.put("riskLevel", riskLevel);

        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
        }

        return response;
    }
}
