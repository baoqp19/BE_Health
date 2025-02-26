package com.example.HealthCare.config;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.example.HealthCare.Util.SercurityUtil;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import static com.example.HealthCare.enums.Permission.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

	@Value("${SECRET_KEY}")
	private String jwtKey;

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

	// trả về danh sách thông tin người dùng và danh sách quyền hạn
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");
		grantedAuthoritiesConverter.setAuthoritiesClaimName("permission");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
	}

	// chuyển key base64 sang Secretkey (khoá bí mật)
	private SecretKey getSecretKey() {
		byte[] keyBytes = Base64.from(jwtKey).decode();
		return new SecretKeySpec(keyBytes, 0, keyBytes.length,
				SercurityUtil.JW_ALGORITHM.getName());
	}

	// dùng để giải mã token
	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
				getSecretKey()).macAlgorithm(SercurityUtil.JW_ALGORITHM).build();
		return token -> {
			try {
				return jwtDecoder.decode(token);
			} catch (Exception e) {
				System.out.println(">>> JWT error: " + e.getMessage());
				throw e;
			}
		};
	}

}
