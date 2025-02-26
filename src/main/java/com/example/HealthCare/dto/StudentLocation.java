package com.example.HealthCare.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentLocation {
    Long id;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
}
