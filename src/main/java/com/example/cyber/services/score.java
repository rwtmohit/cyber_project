package com.example.cyber.services;

import org.springframework.stereotype.Service;

@Service
public class score {
    
    /** 
     * @param sslScore Score from ssl Module (0-40)
     * @param domainScore Score from domain Module (0-60)
     * @param urlanalysisScore Score from urlanalysis Module (0-20)  
     * @return int Total Score (0-100)
     */
    public int calculateTotalScore(int sslScore, int domainScore, int urlanalysisScore) {
        sslScore = Math.min(sslScore, 30);
        domainScore = Math.min(domainScore, 50);
        urlanalysisScore = Math.min(urlanalysisScore, 20);
        return sslScore + domainScore + urlanalysisScore;
        
}
public String classifyRisk(int totalScore){
    if(totalScore >= 70) return "Safe";
    else if (totalScore >= 40) return "Moderate";
    return "High Risk";
}
}
