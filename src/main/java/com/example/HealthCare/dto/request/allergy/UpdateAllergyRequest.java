package com.example.HealthCare.dto.request.allergy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAllergyRequest {
    private Integer id;

    private Integer memberId;

    @NotBlank(message = "Allergy type is required")
    @Size(max = 100, message = "Allergy type must not exceed 100 characters")
    private String allergyType;

    @NotBlank(message = "Severity is required")
    @Size(max = 100, message = "Severity must not exceed 100 characters")
    private String severity;

    @NotBlank(message = "Symptoms is required")
    @Size(max = 300, message = "Symptoms must not exceed 100 characters")
    private String symptoms;
    
}