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

@Service
public class SercurityUtil {
    private final JwtEncoder jwtEncoder;
    public static final MacAlgorithm JW_ALGORITHM = MacAlgorithm.HS512;

    public SercurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Value("${EXPIRATION}")
    private long jwtExpiretion;

    public String createToken(Authentication authenticate) {
        // lấy thời gian ban đầu token
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtExpiretion, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authenticate.getName())
                .claim("baodeptrai", authenticate)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JW_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
