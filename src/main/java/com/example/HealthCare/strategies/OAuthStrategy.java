package com.example.HealthCare.strategies;

import com.example.HealthCare.dto.response.OAuthUser;

public interface OAuthStrategy {
    OAuthUser authenticate(String credential);
}
