package com.example.HealthCare.request.member;

import com.example.HealthCare.enums.BloodGroudEnum;
import com.example.HealthCare.enums.GenderEnum;

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
  private java.time.LocalDate dateOfBirth;

  @Enumerated(EnumType.STRING)
  private GenderEnum gender;

  @NotBlank(message = "Relationship is required")
  private String relationship;

  @NotBlank(message = "BloodType is required")
  @Enumerated(EnumType.STRING)
  private BloodGroudEnum bloodType;

  @NotNull(message = "Height is required")
  @Min(value = 0, message = "Height must be greater than or equal to 0")
  private Float height;

  @NotNull(message = "Weight is required")
  @Min(value = 0, message = "Weight must be greater than or equal to 0")
  private Float weight;
}
