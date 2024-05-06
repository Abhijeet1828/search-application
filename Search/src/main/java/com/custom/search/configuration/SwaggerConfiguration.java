package com.custom.search.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * This class is used to configure Swagger and add important information about
 * the application.
 * 
 * @author Abhijeet
 *
 */
@Configuration
public class SwaggerConfiguration {

	/**
	 * Creating a bean for defining the OpenAPI Swagger's contact and information.
	 * This will displayed on the Swagger UI.
	 * 
	 * @return {@link OpenAPI}
	 */
	@Bean
	public OpenAPI defineOpenAPI() {
		Server server = new Server();
		server.setUrl("http://localhost:8080/search");
		server.setDescription("Development");

		Contact contact = new Contact();
		contact.setName("Abhijeet");
		contact.setEmail("srivastava.abhijeet96@gmail.com");

		Info info = new Info().title("Spring Boot Elasticsearch Project").version("1.0")
				.description("This project provides API for querying elasticsearch").contact(contact);

		List<Server> servers = new ArrayList<>();
		servers.add(server);

		return new OpenAPI().info(info).servers(servers);
	}

}
