package com.northcoders.recordshopAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
public class RecordshopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordshopApiApplication.class, args);
	}

}
