package com.example.cyber.dtos;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String email;
    private String otp;
}
