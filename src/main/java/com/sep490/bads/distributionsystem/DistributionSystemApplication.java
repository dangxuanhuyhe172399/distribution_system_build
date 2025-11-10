package com.sep490.bads.distributionsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DistributionSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(DistributionSystemApplication.class, args);
	}

}
