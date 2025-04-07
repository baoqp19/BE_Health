package com.example.HealthCare.context;

import com.example.HealthCare.dto.response.OAuthUser;
import com.example.HealthCare.strategies.OAuthStrategy;

public class OAuthContext {
    private final OAuthStrategy oAuthStrategy;

    public OAuthContext(OAuthStrategy oAuthStrategy) {
        this.oAuthStrategy = oAuthStrategy;
    }

    public OAuthUser authenticate(String credential) {
        return oAuthStrategy.authenticate(credential);
    }
}
