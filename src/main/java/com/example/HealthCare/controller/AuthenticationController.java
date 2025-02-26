package com.example.HealthCare.controller;

import com.example.HealthCare.request.auth.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  public AuthenticationController(AuthenticationManagerBuilder authenticationManagerBuilder) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginRequest> authenticate(
      @Valid @RequestBody LoginRequest loginRequest) {

    // nạp input gồm username/password vào Security
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(), loginRequest.getPassword());
    // xác thực người dùng => cần viết hàm loadUserByUsername

    Authentication authenticateAction = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    return ResponseEntity.ok().body(loginRequest);
  }

}
