package com.example.HealthCare.Util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.HealthCare.request.auth.ResTokenLogin;

@Service
public class SercurityUtil {


    private final JwtEncoder jwtEncoder;
    
    public static final MacAlgorithm JW_ALGORITHM = MacAlgorithm.HS512;

    public SercurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Value("${EXPIRATION_ACCESS_TOKEN}")
    private long jwtExpiretion;

    @Value("${SECRET_KEY}")
    private String jwtKey;

    @Value("${EXPIRATION_ACCESS_TOKEN}")
    private long accessTokenExpiration;

    @Value("${EXPIRATION_REFRESH_TOKEN}")
    private long refreshTokenExpiration;

    public String createAccessToken(Authentication authenticate) {

        // lấy thời gian ban đầu token
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authenticate.getName())
                .claim("baodeptrai", authenticate)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JW_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String createRefreshToken(String email, ResTokenLogin dto) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(email)
            .claim("user", dto.getUser())
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JW_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }
}
