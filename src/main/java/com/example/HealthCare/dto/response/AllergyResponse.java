package com.example.HealthCare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllergyResponse {
    int id;
    MemberDTO member;
    String allergyType;
    String severity;
    String symptoms;
}