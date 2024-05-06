package com.custom.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This class is used to run the Search Spring Boot application.
 * 
 * @implNote ComponentScan on packages are done to scan the spring annotations
 *           in the common utilities JAR.
 * 
 * @author Abhijeet
 *
 */
@ComponentScan(basePackages = { "com.custom.common.utilities", "com.custom.search" })
@SpringBootApplication
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}

}
