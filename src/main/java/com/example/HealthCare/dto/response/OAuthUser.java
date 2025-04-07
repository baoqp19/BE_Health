package com.example.HealthCare.dto.response;

import com.example.HealthCare.enums.AuthProvider;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthUser {
    private String name;
    private String email;
    private String avatar;
    private AuthProvider provider;
}