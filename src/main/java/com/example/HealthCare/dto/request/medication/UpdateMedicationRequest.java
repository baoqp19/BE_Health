package com.example.HealthCare.dto.request.medication;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateMedicationRequest {

  @NotBlank(message = "Medication name is required")
  @Size(max = 100, message = "Medication name must not exceed 100 characters")
  private String name;

  @NotBlank(message = "Frequency is required")
  private String frequency;

  @NotNull(message = "Start date is required")
  @Past(message = "Start date must be a past date")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private java.time.LocalDate startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private java.time.LocalDate endDate;

  private String note;
}
