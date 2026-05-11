package com.example.cyber.controller;

import com.example.cyber.services.urlanalysis;
import com.example.cyber.services.whoisService;
import com.example.cyber.services.score;
import com.example.cyber.services.ssl;
import com.example.cyber.services.DnsService;
import com.example.cyber.services.header;
import com.example.cyber.model.SSLReport;
import com.example.cyber.model.whoisRecord;
import com.example.cyber.model.DNSRecord;
import com.example.cyber.model.HeaderSecurityResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xbill.DNS.Record;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") 
public class securityCheckerController {

    @Autowired
    private DnsService dnsService;


    @Autowired
    private urlanalysis urlAnalysisService;

    @Autowired
    private score scoreService;

    @Autowired
    private ssl sslService;

    @Autowired
    private header headerService;

    @Autowired
    private whoisService WhoisService;

    @GetMapping("/analyze")
    public Map<String, Object> analyzerWebsite(@RequestParam String url) {

        Map<String, Object> response = new HashMap<>();

        try {
            int urlscore = urlAnalysisService.analyzeURLScore(url);

            SSLReport sslReport = sslService.checkSSL(url);
            int sslscore = (sslReport != null && sslReport.isValid()) ? 40 : 0;

             response.put("sslReport", sslReport);


            HeaderSecurityResult headerResult = headerService.checkSecurityHeaders(url);
            int headerscore = (headerResult != null) ? headerResult.score : 0;

            DNSRecord dnsReport = dnsService.checkDNS(url);;
            int totalscore = scoreService.calculateTotalScore(sslscore, headerscore, urlscore);
            String riskLevel = scoreService.classifyRisk(totalscore);

            response.put("url", url);
            response.put("urlPatternScore", urlscore);
            response.put("sslScore", sslscore);
            response.put("headerScore", headerscore);
            response.put("totalScore", totalscore);
            response.put("risk_level", riskLevel);
            
            
            Map<String, Object> dnsReadable = new HashMap<>();
            dnsReadable.put("A", convertA(dnsReport.getARecords()));
            dnsReadable.put("MX", convertMX(dnsReport.getMXRecords()));
            dnsReadable.put("NS", convertNS(dnsReport.getNSRecords()));
            dnsReadable.put("error", dnsReport.getError());

            response.put("dnsReport", dnsReadable);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    @GetMapping("/whois")
    public whoisRecord getWhois(@RequestParam String domain) {
        return WhoisService.getWhoisInfo(domain);
    }
    // Helper methods to convert DNS records into readable formats
    private List<String> convertA(org.xbill.DNS.Record[] records) {
    List<String> list = new ArrayList<>();
    if (records == null) return list;

    for (Record r : records) {
        if (r instanceof ARecord) {
            list.add(((ARecord) r).getAddress().getHostAddress());
        }
    }
    return list;
}

private List<Map<String, Object>> convertMX(org.xbill.DNS.Record[] records) {
    List<Map<String, Object>> list = new ArrayList<>();
    if (records == null) return list;

    for (Record r : records) {
        if (r instanceof MXRecord) {
            MXRecord mx = (MXRecord) r;
            list.add(Map.of(
                    "priority", mx.getPriority(),
                    "host", mx.getTarget().toString()
            ));
        }
    }
    return list;
}

private List<String> convertNS(org.xbill.DNS.Record[] records) {
    List<String> list = new ArrayList<>();
    if (records == null) return list;

    for (Record r : records) {
        if (r instanceof NSRecord) {
            list.add(((NSRecord) r).getTarget().toString());
        }
    }
    return list;
}

}
