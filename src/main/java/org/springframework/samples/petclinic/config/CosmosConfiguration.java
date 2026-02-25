package org.springframework.samples.petclinic.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCosmosRepositories(basePackages = "org.springframework.samples.petclinic")
public class CosmosConfiguration extends AbstractCosmosConfiguration {

	@Value("${azure.cosmos.uri}")
	private String cosmosUri;

	@Value("${azure.cosmos.database}")
	private String cosmosDatabase;

	@Bean
	public CosmosClientBuilder cosmosClientBuilder() {
		return new CosmosClientBuilder()
			.endpoint(cosmosUri)
			.credential(new DefaultAzureCredentialBuilder().build());
	}

	@Bean
	public CosmosConfig cosmosConfig() {
		return CosmosConfig.builder()
			.enableQueryMetrics(true)
			.build();
	}

	@Override
	protected String getDatabaseName() {
		return cosmosDatabase;
	}

}
