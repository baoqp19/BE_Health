package com.example.HealthCare.model;

import com.example.HealthCare.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue
  private Integer id;

  private String firstname;

  private String lastname;

  @Column(unique = true)
  private String email;
  private String password;

  @Column(name = "is_block", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean is_block;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "is_verify", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean is_verify;

  @Column(columnDefinition = "MEDIUMTEXT")
  private String refreshToken;


  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Token> tokens;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Member> members;


}
