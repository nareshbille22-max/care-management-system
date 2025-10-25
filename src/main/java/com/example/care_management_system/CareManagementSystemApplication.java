package com.example.care_management_system;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CareManagementSystemApplication {

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        SpringApplication.run(CareManagementSystemApplication.class, args);
	}

}
