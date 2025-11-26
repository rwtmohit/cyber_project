package com.example.securitychecker.service;

import com.example.cyber.model.DNSRecord;
import com.example.cyber.services.domain;
import org.xbill.DNS.Record;

public class DomainService {

    public DNSInfo getDNSRecords(String domainName) {
        domain d = new domain();
        DNSRecord rec = d.getDNS(domainName);
        return new DNSInfo(rec);
    }

    public static class DNSInfo {
        private final Record[] a;
        private final Record[] mx;
        private final Record[] ns;

        public DNSInfo(DNSRecord r) {
            this.a = (r != null) ? r.getARecords() : null;
            this.mx = (r != null) ? r.getMXRecords() : null;
            this.ns = (r != null) ? r.getNSRecords() : null;
        }

        public Record[] getA() { return a; }
        public Record[] getMx() { return mx; }
        public Record[] getNs() { return ns; }
    }
}
