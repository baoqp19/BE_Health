package com.example.HealthCare.service;

import com.example.HealthCare.dto.response.AuthenticationResponse;

public interface OAuthService {
    AuthenticationResponse authenticate(String credential);
}
