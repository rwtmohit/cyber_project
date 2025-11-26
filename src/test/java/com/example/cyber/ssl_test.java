package com.example.cyber;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.security.cert.Certificate;

public class ssl_test {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://google.com");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.connect();
        System.out.println("Connected: Response code = " + con.getResponseCode());
         for (Certificate cert : con.getServerCertificates()) {
        System.out.println(cert.toString());}
        con.disconnect();
    
         }
         
}
