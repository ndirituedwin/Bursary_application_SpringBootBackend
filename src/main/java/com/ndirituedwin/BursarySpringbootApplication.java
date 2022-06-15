package com.ndirituedwin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {BursarySpringbootApplication.class, Jsr310JpaConverters.class})
@EnableAsync
public class BursarySpringbootApplication {

	@PostConstruct
	void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {

		SpringApplication.run(BursarySpringbootApplication.class, args);
	}

}
