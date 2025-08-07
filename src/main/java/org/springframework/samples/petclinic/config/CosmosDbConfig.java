package org.springframework.samples.petclinic.config;

import com.azure.cosmos.DirectConnectionConfig;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cosmos")
@EnableCosmosRepositories(basePackages = "org.springframework.samples.petclinic")
public class CosmosDbConfig extends AbstractCosmosConfiguration {

	@Value("${azure.cosmos.uri}")
	private String uri;

	@Value("${azure.cosmos.key}")
	private String key;

	@Value("${azure.cosmos.database}")
	private String databaseName;

	@Bean
	public CosmosClientBuilder cosmosClientBuilder() {
		DirectConnectionConfig directConnectionConfig = DirectConnectionConfig.getDefaultConfig();
		return new CosmosClientBuilder().endpoint(uri)
			.credential(new DefaultAzureCredentialBuilder().build())
			.directMode(directConnectionConfig);
	}

	@Override
	protected String getDatabaseName() {
		return "database";
	}

}
