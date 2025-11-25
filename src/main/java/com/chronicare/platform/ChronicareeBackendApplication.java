package com.chronicare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * ChroniCare Backend Application
 * @summary
 * Main application class for the ChroniCare Healthcare Platform
 * Enables JPA Auditing for automatic timestamp management
 */
@SpringBootApplication
@EnableJpaAuditing
public class ChronicareeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChronicareeBackendApplication.class, args);
	}

}
