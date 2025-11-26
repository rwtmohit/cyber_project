package com.example.cyber.model;

public class DomainReport {
    private String domain;
    private String whoisData;
    private DNSRecord dnsRecord;

    public DomainReport(String domain, String whoisData, DNSRecord dnsRecord) {
        this.domain = domain;
        this.whoisData = whoisData;
        this.dnsRecord = dnsRecord;
    }

    public String getDomain() {
        return domain;
    }

    public String getWhoisData() {
        return whoisData;
    }

    public DNSRecord getDnsRecord() {
        return dnsRecord;
    }
}
