package com.github.stoynko.easydoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EasyDoc {

	public static void main(String[] args) {
		SpringApplication.run(EasyDoc.class, args);
	}

}
