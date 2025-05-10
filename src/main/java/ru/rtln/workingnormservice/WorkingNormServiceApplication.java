package ru.rtln.workingnormservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WorkingNormServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkingNormServiceApplication.class, args);
	}

}
