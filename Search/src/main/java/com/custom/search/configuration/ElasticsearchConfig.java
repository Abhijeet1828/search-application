package com.custom.search.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * This class is used to configure the connection between the spring boot
 * application and elasticsearch.
 * 
 * @implNote It also scans the elasticsearch repositories in the specified
 *           package.
 * 
 * @author Abhijeet
 *
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.custom.search.es.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

	@Value("${spring.elasticsearch.host}")
	String elasticHost;

	@Value("${spring.elasticsearch.ca.fingerprint}")
	String elasticCaFingerprint;

	@Value("${spring.elasticsearch.username}")
	String elasticUsername;

	@Value("${spring.elasticsearch.password}")
	String elasticPassword;

	/**
	 * The elasticsearch client is configured using the host address, the HTTP
	 * self-signed certificate's SHA-256 signature, username and password.
	 */
	@Override
	public ClientConfiguration clientConfiguration() {
		return ClientConfiguration.builder().connectedTo(elasticHost).usingSsl(elasticCaFingerprint)
				.withBasicAuth(elasticUsername, elasticPassword).build();
	}

}
