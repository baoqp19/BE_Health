package com.example.HealthCare.controller;

import com.example.HealthCare.Util.SercurityUtil;
import com.example.HealthCare.request.auth.LoginRequest;
import com.example.HealthCare.request.auth.ResTokenLogin;

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
public class AuthController {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SercurityUtil sercurityUtil;

  public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SercurityUtil sercurityUtil) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.sercurityUtil = sercurityUtil;
  }

  @PostMapping("/login")
  public ResponseEntity<ResTokenLogin> login(@Valid @RequestBody LoginRequest loginRequest) {

    // nạp input gồm username/ password vào Security
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(), loginRequest.getPassword());
    // xác thực người dùng => cần viết hàm loadUserByUsername

    Authentication authenticateAction = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    String access_token = this.sercurityUtil.createToken(authenticateAction);
    ResTokenLogin res = new ResTokenLogin();
    res.setAccessToken(access_token);
    return ResponseEntity.ok().body(res);

  }

}
