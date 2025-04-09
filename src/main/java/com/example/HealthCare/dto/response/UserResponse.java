package com.example.HealthCare.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    Integer id;
    String firstname;
    String lastname;

    @JsonProperty("email")
    String email;

    Boolean is_verify;
    Boolean is_block;

}
