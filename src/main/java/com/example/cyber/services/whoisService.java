package com.example.cyber.services;
import com.example.cyber.model.whoisRecord;

import org.apache.commons.net.whois.WhoisClient;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class whoisService {
    
    public whoisRecord getWhoisInfo(String domain) {
        whoisRecord record = new whoisRecord();
        try {
            WhoisClient whois = new WhoisClient();

            whois.connect(WhoisClient.DEFAULT_HOST);

            String result = whois.query(domain);

            whois.disconnect();

            record.setDomainOwner(extractValue(result,
                    "Registrant Organization:",
                    "Registrant Name:",
                    "OrgName:"));

                    record.setCreationDate(extractValue(result,
                    "Creation Date:",  
                     "Created On:",
                    "Registered On:")); 

                    record.setExpiryDate(extractValue(result,
                    "Registry Expiry Date:",
                    "Registrar Registration Expiration Date:",
                    "Expiration Date:"));

            record.setRegistrar(extractValue(result,
                    "Registrar:",
                    "Sponsoring Registrar:"));

            record.setCountry(extractValue(result,
                    "Registrant Country:",
                    "Country:"));
}
catch (IOException e) {
            throw new RuntimeException("WHOIS lookup failed: " + e.getMessage());
}
 return record;
    }

    private String extractValue(String whoisData, String... keys) {
        for (String key : keys) {
            Pattern pattern = Pattern.compile(key + "\\s*(.*)");
            Matcher matcher = pattern.matcher(whoisData);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        }
        return "N/A";
    }
}