package com.example.cyber.model;

import java.time.LocalDate;

public class SSLReport {
    private String host;
    private String issuerDN;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean valid;
    private String issuer;

    public SSLReport(String host, String issuerDN, LocalDate startDate, LocalDate endDate, boolean valid, String issuer) {
        this.host = host;
        this.issuerDN = issuerDN;
        this.startDate = startDate;
        this.endDate = endDate;
        this.valid = valid;
        this.issuer = issuer;
    }

    public String getHost() { return host; }
    public String getIssuerDN() { return issuerDN; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isValid() { return valid; }
    public String getIssuer() { return issuer; }
}
