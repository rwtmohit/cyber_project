package com.example.cyber.services;

import com.example.cyber.model.DNSRecord;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Type;

@Service
public class DnsService {

    // Extract domain from URL
    public String extractDomain(String url) {
        try {
            if (!url.startsWith("http")) {
                url = "https://" + url;
            }
            String host = new java.net.URI(url).getHost();
            return (host != null && host.startsWith("www.")) ? host.substring(4) : host;
        } catch (Exception e) {
            return null;
        }
    }

    // Perform DNS lookup for a specific type
    private org.xbill.DNS.Record[] lookup(String domain, int type) {
        try {
            Lookup lookup = new Lookup(domain, type);
            org.xbill.DNS.Record[] records = lookup.run();
            return records != null ? records : new org.xbill.DNS.Record[0];
        } catch (Exception e) {
            return new org.xbill.DNS.Record[0];
        }
    }

    // MAIN method called by controller
    public DNSRecord checkDNS(String url) {
        DNSRecord record = new DNSRecord();

        String domain = extractDomain(url);
        if (domain == null) {
            record.setError("Invalid URL");
            return record;
        }

        // Use lookup() correctly
        record.setARecords(lookup(domain, Type.A));
        record.setMXRecords(lookup(domain, Type.MX));
        record.setNSRecords(lookup(domain, Type.NS));

        return record;
    }
}
