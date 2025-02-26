package com.example.HealthCare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
// @EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class HealthCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthCareApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner commandLineRunner(
	// 		AuthenticationService service,
	// 		UserRepository userRepository) {
	// 	return args -> {
	// 		registerIfNotExists(service, userRepository, createAdminRequest());
	// 		registerIfNotExists(service, userRepository, createManagerRequest());
	// 	};
	// }

	// private void registerIfNotExists(AuthenticationService service, UserRepository userRepository,
	// 		RegisterRequest request) {
	// 	if (!userRepository.existsByEmail(request.getEmail())) {
	// 		String token = service.register(request).getAccessToken();
	// 		System.out.println(request.getRole() + " token: " + token);
	// 	} else {
	// 		System.out.println("User with email " + request.getEmail() + " already exists.");
	// 	}
	// }

	// private RegisterRequest createAdminRequest() {
	// 	return RegisterRequest.builder()
	// 			.firstname("Admin")
	// 			.lastname("Admin")
	// 			.email("admin@mail.com")
	// 			.password("12345678")
	// 			.role(Role.ADMIN)
	// 			.build();
	// }

	// private RegisterRequest createManagerRequest() {
	// 	return RegisterRequest.builder()
	// 			.firstname("User")
	// 			.lastname("User")
	// 			.email("user@mail.com")
	// 			.password("12345678")
	// 			.role(Role.USER)
	// 			.build();
	// }
}