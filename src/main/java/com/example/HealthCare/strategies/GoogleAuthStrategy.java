package com.example.HealthCare.strategies;

import com.example.HealthCare.dto.response.GoogleUserInfo;
import com.example.HealthCare.dto.response.OAuthUser;
import com.example.HealthCare.enums.AuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class GoogleAuthStrategy implements OAuthStrategy {
    @Override
    public OAuthUser authenticate(String credential) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + credential;
        GoogleUserInfo googleUserInfo = restTemplate.getForObject(url, GoogleUserInfo.class);

        assert googleUserInfo != null;
        return new OAuthUser(googleUserInfo.getName(), googleUserInfo.getEmail(), googleUserInfo.getPicture(), AuthProvider.GOOGLE);

    }
}
