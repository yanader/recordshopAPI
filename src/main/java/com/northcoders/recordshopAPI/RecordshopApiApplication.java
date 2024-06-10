package com.northcoders.recordshopAPI;

import com.northcoders.recordshopAPI.view.UserInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class RecordshopApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(RecordshopApiApplication.class, args);
		// new UserInterface().run();
	}

}
