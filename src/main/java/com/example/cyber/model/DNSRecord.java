package com.example.cyber.model;

import org.xbill.DNS.Record;

public class DNSRecord {

    private String error;
    private Record[] aRecords;
    private Record[] mxRecords;
    private Record[] nsRecords;

    // ---- GETTERS & SETTERS ---- //

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Record[] getARecords() {
        return aRecords;
    }

    public void setARecords(Record[] aRecords) {
        this.aRecords = aRecords;
    }

    public Record[] getMXRecords() {
        return mxRecords;
    }

    public void setMXRecords(Record[] mxRecords) {
        this.mxRecords = mxRecords;
    }

    public Record[] getNSRecords() {
        return nsRecords;
    }

    public void setNSRecords(Record[] nsRecords) {
        this.nsRecords = nsRecords;
    }
}
