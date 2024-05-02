package com.custom.search.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

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

	@Override
	public ClientConfiguration clientConfiguration() {
		return ClientConfiguration.builder().connectedTo(elasticHost).usingSsl(elasticCaFingerprint)
				.withBasicAuth(elasticUsername, elasticPassword).build();
	}

}
