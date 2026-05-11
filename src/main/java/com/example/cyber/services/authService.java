package com.example.cyber.services;

import com.example.cyber.dtos.LoginRequest;
import com.example.cyber.dtos.RegisterRequest;
import com.example.cyber.dtos.VerifyOtpRequest;
import com.example.cyber.entity.user;
import com.example.cyber.repository.userrepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class authService {
    private final userrepository userRepository;
    // private final PasswordEncoder passwordEncoder; // Commented out to avoid missing bean error
    private final emailService emailService;

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already registered";
        }
        String otp = generateOtp();
        user userObj = user.builder()
                .email(request.getEmail())
                // .password(passwordEncoder.encode(request.getPassword())) // Commented out
                .password(request.getPassword()) // Store plain password (not secure, for demo only)
                .verified(false)
                .otp(otp)
                .otpExpiry(LocalDateTime.now().plusMinutes(5))
                .build();
        userRepository.save(userObj);
        emailService.sendOtp(userObj.getEmail(), otp);
        return "OTP sent to email";
    }

    public String verifyOtp(VerifyOtpRequest request) {
        user userObj = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (userObj.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP expired";
        }
        if (!userObj.getOtp().equals(request.getOtp())) {
            return "Invalid OTP";
        }
        userObj.setVerified(true);
        userObj.setOtp(null);
        userObj.setOtpExpiry(null);
        userRepository.save(userObj);
        return "Email verified successfully";
    }

    public String login(LoginRequest request) {
        user userObj = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!userObj.isVerified()) {
            return "Please verify email first";
        }
        // if (!passwordEncoder.matches(request.getPassword(), userObj.getPassword())) { // Commented out
        if (!request.getPassword().equals(userObj.getPassword())) { // Plain text check (not secure)
            return "Invalid password";
        }
        return "Login successful";
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
