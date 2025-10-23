package com.example.care_management_system.dto;

import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
public class JwtResponse {
    private String token;
    private String expiry; // human-readable date string

    public JwtResponse(String token, long expiryMillis) {
        this.token = token;
        this.expiry = convertMillisToReadableDate(expiryMillis);
    }

    private String convertMillisToReadableDate(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
