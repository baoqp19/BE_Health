package com.example.HealthCare.controller;

import com.example.HealthCare.Util.SercurityUtil;
import com.example.HealthCare.model.User;
import com.example.HealthCare.request.auth.LoginRequest;
import com.example.HealthCare.request.auth.ResTokenLogin;
import com.example.HealthCare.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

  @Value("${EXPIRATION_REFRESH_TOKEN}")
  private long refreshTokenExpiration;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SercurityUtil sercurityUtil;
  private final UserService userService;

  public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SercurityUtil sercurityUtil,
      UserService userService) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.sercurityUtil = sercurityUtil;
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<ResTokenLogin> login(@Valid @RequestBody LoginRequest loginRequest) {

    // nạp input gồm username/ password vào Security
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(), loginRequest.getPassword());
    // xác thực người dùng => cần viết hàm loadUserByUsername

    Authentication authenticateAction = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    String access_token = this.sercurityUtil.createAccessToken(authenticateAction);

    ResTokenLogin res = new ResTokenLogin();

    User currentUserDB = this.userService.handleGetUserByEmail(loginRequest.getEmail());
    if (currentUserDB != null) {
      ResTokenLogin.UserLogin userLogin = new ResTokenLogin.UserLogin(
          currentUserDB.getId(),
          currentUserDB.getEmail(),
          currentUserDB.getFirstname(),
          currentUserDB.getLastname());
      res.setUser(userLogin);
    }
    res.setAccessToken(access_token);

    String refresh_token = this.sercurityUtil.createRefreshToken(loginRequest.getEmail(), res);

    // update user
    this.userService.updateUserToken(refresh_token, loginRequest.getEmail());
    // set cookies
    ResponseCookie resCookies = ResponseCookie
        .from("refresh_token", refresh_token)
        .httpOnly(true) // chỉ cho server của to sử dụng
        .secure(true) // có nghĩa là cookies chỉ được sử dụng với https (http kh)
        .path("/") // tất cả các api đều trả về cookie
        .maxAge(refreshTokenExpiration) // thời gian hết hạn từ khi chạy
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, resCookies.toString())

        .body(res);
  }

}
