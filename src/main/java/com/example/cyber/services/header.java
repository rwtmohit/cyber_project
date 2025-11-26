package com.example.cyber.services;
import com.example.cyber.model.HeaderSecurityResult;
import org.springframework.stereotype.Service;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.*;


@Service
public class header {

    private static final List<String> SECURITY_HEADERS = Arrays.asList(
            "Content-Security-Policy",
            "Strict-Transport-Security",
            "X-Content-Type-Options",
            "X-Frame-Options",
            "X-XSS-Protection",
            "Referrer-Policy",
            "Permissions-Policy"
    );
    //check security headers
    public HeaderSecurityResult checkSecurityHeaders(String websiteUrl) {
        try{
            if(!websiteUrl.startsWith("http://") && !websiteUrl.startsWith("https://")){
                websiteUrl = "http://" + websiteUrl;
            }

            URL url = new URL(websiteUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.connect();

            Map<String, List<String>> responseHeaders = connection.getHeaderFields();
            List<String> missingHeaders = new ArrayList<>();    
            int score = 0;

            for(String header : SECURITY_HEADERS){
                if(!responseHeaders.containsKey(header)){
                    missingHeaders.add(header);
                }
            else {
                score += 10;
            }
        
        }
        //cookies safety check
        boolean secureCookies =false;
        if (responseHeaders.containsKey("Set-Cookie")){
            for(String cookie : responseHeaders.get("Set-Cookie")){
                if(cookie.contains("Secure") && cookie.contains("HttpOnly")){
                    secureCookies = true;
                    score += 15;
                    break;
                }
            }
        }

    //Assign risk level based on score

    String riskLevel;
    if(score >= 70){
        riskLevel = "Safe";
    } else if (score >= 40) {
        riskLevel = "Moderate Risk";
    } else {
        riskLevel = "High Risk";
    }
   connection.disconnect();

   //return result
    return new HeaderSecurityResult(websiteUrl,score, riskLevel, missingHeaders, secureCookies);
          }
            catch(Exception e){
                return new HeaderSecurityResult(websiteUrl, 0, "Error: "+ e.getMessage(), Collections.emptyList(), false);
            }
     }


}
