package com.example.cyber.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class user {
    @Id
    private String id;

    private String email;
    private String password;
    private boolean verified;
    private String otp;
    private LocalDateTime otpExpiry;
}
