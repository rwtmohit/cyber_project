package com.example.cyber;
import com.example.securitychecker.service.DomainService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class domain_test {
      private final DomainService domainService = new DomainService();

    @Test
    void testDNSLookup() {
        String domain = "google.com";

        var dns = domainService.getDNSRecords(domain);

        System.out.println("A Records: " + (dns.getA() != null));
        System.out.println("MX Records: " + (dns.getMx() != null));
        System.out.println("NS Records: " + (dns.getNs() != null));

        // At least NS records should always exist
        assertNotNull(dns.getNs());
    }
}
    

