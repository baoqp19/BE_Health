package com.example.HealthCare.request.member;

import java.time.Instant;
import java.time.LocalDateTime;

import com.example.HealthCare.enums.BloodGroudEnum;
import com.example.HealthCare.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddMemberRequest {

  private Integer memberID;

  private Integer userID;

  @NotBlank(message = "FullName is required")
  @Size(max = 100, message = "FullName must not exceed 100 characters")
  private String fullName;

  @NotNull(message = "DateOfBirth is required")
  @Past(message = "DateOfBirth must be a past date")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateOfBirth;

  @Enumerated(EnumType.STRING)
  private GenderEnum gender;

  @NotBlank(message = "Relationship is required")
  private String relationship;

  @Enumerated(EnumType.STRING)
  private BloodGroudEnum bloodType;

  @NotNull(message = "Height is required")
  @Min(value = 0, message = "Height must be greater than or equal to 0")
  private Float height;

  @NotNull(message = "Weight is required")
  @Min(value = 0, message = "Weight must be greater than or equal to 0")
  private Float weight;
}
