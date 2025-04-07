package com.example.HealthCare.service.impl;

import com.example.HealthCare.context.OAuthContext;
import com.example.HealthCare.dto.response.OAuthUser;
import com.example.HealthCare.repository.UserRepository;
import com.example.HealthCare.service.OAuthService;
import com.example.HealthCare.strategies.GoogleAuthStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class OAuthServiceImpl implements OAuthService {

    private UserRepository userRepository;

    public OAuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String authenticate(String credential) {
        log.info("Google login credential: {}", credential);
        OAuthContext oAuthContext = new OAuthContext(new GoogleAuthStrategy());
        OAuthUser oauthUser = oAuthContext.authenticate(credential);
        return oauthUser.getEmail();
    }
}
