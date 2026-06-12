package com.example.cyber.dtos;

import java.util.List;

public class UrlScanResponse {

    private String url;
    private String status;

    private int malicious;
    private int suspicious;
    private int harmless;
    private int undetected;

    private int totalVendors;
    private double riskPercentage;

    private List<String> flaggedBy;

    public UrlScanResponse() {
    }

    public UrlScanResponse(
            String url,
            String status,
            int malicious,
            int suspicious,
            int harmless,
            int undetected,
            int totalVendors,
            double riskPercentage,
            List<String> flaggedBy) {

        this.url = url;
        this.status = status;
        this.malicious = malicious;
        this.suspicious = suspicious;
        this.harmless = harmless;
        this.undetected = undetected;
        this.totalVendors = totalVendors;
        this.riskPercentage = riskPercentage;
        this.flaggedBy = flaggedBy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMalicious() {
        return malicious;
    }

    public void setMalicious(int malicious) {
        this.malicious = malicious;
    }

    public int getSuspicious() {
        return suspicious;
    }

    public void setSuspicious(int suspicious) {
        this.suspicious = suspicious;
    }

    public int getHarmless() {
        return harmless;
    }

    public void setHarmless(int harmless) {
        this.harmless = harmless;
    }

    public int getUndetected() {
        return undetected;
    }

    public void setUndetected(int undetected) {
        this.undetected = undetected;
    }

    public int getTotalVendors() {
        return totalVendors;
    }

    public void setTotalVendors(int totalVendors) {
        this.totalVendors = totalVendors;
    }

    public double getRiskPercentage() {
        return riskPercentage;
    }

    public void setRiskPercentage(double riskPercentage) {
        this.riskPercentage = riskPercentage;
    }

    public List<String> getFlaggedBy() {
        return flaggedBy;
    }

    public void setFlaggedBy(List<String> flaggedBy) {
        this.flaggedBy = flaggedBy;
    }
}