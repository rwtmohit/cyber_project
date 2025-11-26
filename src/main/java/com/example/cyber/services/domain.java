package com.example.cyber.services;
import org.apache.commons.net.whois.WhoisClient;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;
import com.example.cyber.model.DNSRecord;
import com.example.cyber.model.DomainReport;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class domain {
    //extract core domain from url
    public String extractDomain(String url){
        try{
            //convert string to url
            URI uri= new URI(url);
            String host = uri.getHost();//extract only hostname 
            return (host != null && host.startsWith("www.")) ? host.substring(4) : host;
         
        }
        catch(Exception e){
            return "Invalid url";
        }
    }
    public String getWhois(String domain){
        try{
            //create whois client 
            WhoisClient client = new WhoisClient();
            return client.query(domain);
            
        }
        catch(Exception e)
        {
            return "whois lookup failed"+e.getMessage();
        }

    }
    
     public LocalDate extractCreationDate(String whoisText) {
        try {
            // Find creation date line
            for (String line : whoisText.split("\n")) {
                if (line.toLowerCase().contains("creation") || line.toLowerCase().contains("created")) {

                    // Extract only the date part
                    String dateText = line.replaceAll("[^0-9-]", "").trim();

                    // Convert to LocalDate
                    return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
            }
        } catch (Exception ignored) {}
        
       
        return null;
    }
    public DNSRecord getDNS(String domain){
        DNSRecord record = new DNSRecord();

        try{
            // Lookup IP address
            Record[] aRecords = new Lookup(domain, Type.A).run();
            record.setARecords(aRecords);

            // Lookup mail server
            Record[] mxRecords = new Lookup(domain, Type.MX).run();
            record.setMXRecords(mxRecords);

            //Lookup name server
            Record[] nsRecords = new Lookup(domain, Type.NS).run();
            record.setNSRecords(nsRecords);

        }
        catch(Exception e){
            record.setError("DNS Lookup Failed:"+ e.getMessage());
        }
        return record;
    }

    public DomainReport scanDomain(String url){
        //extract doamin
        String domain = extractDomain(url);
        //whois lookup
        String whoisData = getWhois(domain);
        //dns records
        DNSRecord dnsRecord = getDNS(domain);
        //creation date (not stored in current DomainReport model)
        //LocalDate creationDate = extractCreationDate(whoisData);

        return new DomainReport(domain, whoisData, dnsRecord);
    }
}


