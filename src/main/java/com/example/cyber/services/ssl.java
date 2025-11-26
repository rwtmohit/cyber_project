package com.example.cyber.services;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.stereotype.Service;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import com.example.cyber.model.SSLReport;



@Service
public class ssl {

    public SSLReport checkSSL(String url){
        String host = url;
        try{
            //remove scheme if present
            host = url.replace("http://","" ).replace("https://","" ).replaceAll("/.*$","" );

            //connection to 443
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(host, 443);
            socket.startHandshake();

            //certificate details
            SSLSession session = socket.getSession();
            Certificate[] certs = session.getPeerCertificates();
            X509Certificate cert = (X509Certificate) certs[0];
            String issuer = cert.getIssuerX500Principal().getName();
            
            //validity dates
            LocalDate startDate = cert.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = cert.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            //check its validity
            boolean isValid = LocalDate.now().isBefore(endDate) && LocalDate.now().isAfter(startDate);
                
            // use X500Principal name instead of deprecated getIssuerDN()
            return new SSLReport(host, issuer, startDate, endDate, isValid, issuer);

        } catch(Exception e){
            return new SSLReport(host, "NO SSL(Invalid SSL)", null, null, false, "Error: "+e.getMessage());
        }
    }
    
}
