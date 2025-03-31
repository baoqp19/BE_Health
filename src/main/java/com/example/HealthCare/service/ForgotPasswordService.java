package com.example.HealthCare.service;

public interface ForgotPasswordService {
    String sendOTP(String email);
    String sendNewPassword(String email, String otp);
}
