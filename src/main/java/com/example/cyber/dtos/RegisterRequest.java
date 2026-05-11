package com.example.cyber.dtos;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
