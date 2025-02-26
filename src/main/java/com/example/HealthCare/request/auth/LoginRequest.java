package com.example.HealthCare.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Getter
@Setter
public class LoginRequest {

  @NotBlank(message = "email không được để trổng")
  @Email(message = "email phải đúng đing dạng")
  private String email;

  @NotBlank(message = "password không được để trống")
  String password;
}
