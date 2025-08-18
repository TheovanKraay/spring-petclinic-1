/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Azure Cosmos DB
 *
 * @author Theovan Kraay
 */
@Configuration
@EnableCosmosRepositories(basePackages = "org.springframework.samples.petclinic")
public class CosmosDbConfiguration extends AbstractCosmosConfiguration {

	@Value("${azure.cosmos.uri}")
	private String uri;

	@Value("${azure.cosmos.database}")
	private String databaseName;

	@Bean
	public CosmosClientBuilder getCosmosClientBuilder() {
		// Use Azure AD authentication with DefaultAzureCredential
		// This will automatically discover credentials from various sources:
		// - Environment variables (AZURE_CLIENT_ID, AZURE_CLIENT_SECRET, AZURE_TENANT_ID)
		// - Managed Identity (when running on Azure)
		// - Azure CLI (when logged in locally)
		// - Visual Studio Code Azure Account extension
		// - IntelliJ Azure Toolkit
		com.azure.identity.DefaultAzureCredential credential = new com.azure.identity.DefaultAzureCredentialBuilder()
			.build();
		return new CosmosClientBuilder().endpoint(uri).credential(credential);
	}

	@Bean
	public CosmosConfig cosmosConfig() {
		return CosmosConfig.builder().enableQueryMetrics(true).build();
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

}
