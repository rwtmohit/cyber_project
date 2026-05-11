package com.example.cyber.services;

import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import com.example.cyber.model.SSLReport;



@Service
public class ssl {

    public SSLReport checkSSL(String url){
    String host;
    
    try {
        URI uri = new URI(url);
        host = (uri.getHost() != null) ? uri.getHost() : url;
    } catch (Exception e) {
        host = url;
    }

    try {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) factory.createSocket(host, 443);

        // Enable SNI
        SSLParameters sslParams = socket.getSSLParameters();
        sslParams.setServerNames(List.of(new SNIHostName(host)));
        socket.setSSLParameters(sslParams);

        socket.startHandshake();

        SSLSession session = socket.getSession();
        X509Certificate cert = (X509Certificate) session.getPeerCertificates()[0];

        String issuer = cert.getIssuerX500Principal().getName();
        LocalDate startDate = cert.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = cert.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        boolean isValid = LocalDate.now().isBefore(endDate) && LocalDate.now().isAfter(startDate);

        return new SSLReport(host, issuer, startDate, endDate, isValid, issuer);

    } catch(Exception e){
        return new SSLReport(host, "NO SSL (Invalid SSL)", null, null, false, "Error: "+e.getMessage());
    }

    }
    
}
