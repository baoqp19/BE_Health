package com.example.HealthCare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.HealthCare.enums.Permission.*;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {



	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static final String[] WHITE_LIST_URL = {
			"/api/v1/auth/**",
			"/api/v1/oauth2/**",
			"/v2/api-docs",
			"/v3/api-docs",
			"/v3/api-docs/**",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-ui.html",
			"/api/v1/openai/ask"
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			CustomLogoutHandler customLogoutHandler) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(WHITE_LIST_URL).permitAll() // Cho phép truy cập danh sách URL mở

						.requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
						.requestMatchers(HttpMethod.POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
						.requestMatchers(HttpMethod.PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
						.requestMatchers(HttpMethod.DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())

						.requestMatchers(HttpMethod.GET, "/api/v1/auth/me").authenticated()

						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// .authenticationProvider(authenticationProvider) // Cung cấp
				// AuthenticationProvider
				// .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

				.logout(logout -> logout
						.logoutUrl("/api/v1/auth/logout") // Định nghĩa URL logout
						.addLogoutHandler(customLogoutHandler) // Xử lý logout tùy chỉnh
						.logoutSuccessHandler(
								(request, response, authentication) -> SecurityContextHolder.clearContext()))
				.build();
	}
}
