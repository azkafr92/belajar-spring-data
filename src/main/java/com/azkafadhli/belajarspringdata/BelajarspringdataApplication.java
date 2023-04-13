package com.azkafadhli.belajarspringdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BelajarspringdataApplication {

	public static void main(String[] args) {
		SpringApplication.run(BelajarspringdataApplication.class, args);
	}

}
