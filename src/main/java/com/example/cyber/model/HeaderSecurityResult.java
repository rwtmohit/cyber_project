package com.example.cyber.model;

import java.util.List;

public class HeaderSecurityResult {
    public String url;
    public int score;
    public String riskLevel;
    public List<String> missingHeaders;
    public boolean secureCookies;

    public HeaderSecurityResult(String url, int score, String riskLevel, List<String> missingHeaders, boolean secureCookies) {
        this.url = url;
        this.score = score;
        this.riskLevel = riskLevel;
        this.missingHeaders = missingHeaders;
        this.secureCookies = secureCookies;
    }
    
}
