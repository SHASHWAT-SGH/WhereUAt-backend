package com.whereuat_app.whereuat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class WhereuatBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereuatBackendApplication.class, args);
	}

}
