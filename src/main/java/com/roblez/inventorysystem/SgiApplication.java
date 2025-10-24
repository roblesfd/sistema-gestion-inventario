package com.roblez.inventorysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
public class SgiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
		System.setProperty("SMTP_USERNAME", dotenv.get("SMTP_USERNAME"));
		System.setProperty("SMTP_PASSWORD", dotenv.get("SMTP_PASSWORD"));
		System.setProperty("PHONE_NUMBER", dotenv.get("PHONE_NUMBER"));
		System.setProperty("CARRIER_GATEWAY", dotenv.get("CARRIER_GATEWAY"));
		System.setProperty("EMAIL_RECIPIENT", dotenv.get("EMAIL_RECIPIENT"));
		
		SpringApplication.run(SgiApplication.class, args);
	}

}
