package com.example.cyber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class UrlResponse {
    private String inputUrl;
    private boolean valid;
    private String message;
    private int length;
    private Map<String, Object> details;
}