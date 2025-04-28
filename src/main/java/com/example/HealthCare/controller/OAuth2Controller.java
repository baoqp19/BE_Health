package com.example.HealthCare.controller;

import com.example.HealthCare.dto.request.google.GoogleLoginRequest;
import com.example.HealthCare.dto.response.ApiResponse;
import com.example.HealthCare.dto.response.AuthenticationResponse;
import com.example.HealthCare.service.OAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/oauth2")
@Slf4j
public class OAuth2Controller {

    private final OAuthService oAuthService;


    public OAuth2Controller(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        return ResponseEntity.ok(this.oAuthService.authenticate(request.getCredential()));
    }
}
