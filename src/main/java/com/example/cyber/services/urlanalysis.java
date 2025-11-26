package com.example.cyber.services;

import org.springframework.stereotype.Service;

@Service
public class urlanalysis {
    
    //Lists of  malicious keywords and domains
    private static final String[] MALICIOUS_KEYWORDS = {
        ".xyz", ".top", ".tk", ".ml", ".cf", ".click", ".info"
    };


        public int analyzeURLScore(String url) {
        int score = 30;
        url = url.toLowerCase();

     //Rule 1: check ip-based URL
    if (url.matches("https?://(\\d{1,3}\\.){3}\\d{1,3}(/.*)?")) {
            score -= 20;  }
    
    //rule 2 : suspicious keywords(symbols)
    if(url.contains("@") || url.contains("//") || url.contains("-") || url.contains("?") || url.contains("%") || url.contains(":") || url.contains("=") ){
        score -=10;         
    }

    //  RULE 3: Check URLlength (too long = suspicious)
        if (url.length() > 80) {
            score -= 10;
        }

    //  RULE 4: Risky domain extensions 
        for (String risky : MALICIOUS_KEYWORDS) {
            if (url.endsWith(risky)) {
                score -= 15;
                break;
            }
        }

    //  RULE 5: Detect fake brand impersonation
        if (url.contains("login-security") || url.contains("verify-account")
                || url.contains("paypal-secure") || url.contains("banking-update")) {
            score -= 20;
        }

        // Score cannot go below 0
        return Math.max(score, 0);

    }

    public String evaluateURL(String url){
        int score = analyzeURLScore(url);
        if(score >= 20) return "Safe";
        else if (score >= 10) return "Moderate";
        return "High Risk";
    }
}

