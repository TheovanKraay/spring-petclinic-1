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

package org.springframework.samples.petclinic.owner;

import java.util.List;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.core.query.CosmosQuery;

/**
 * Repository class for <code>Pet</code> domain objects.
 */
public interface PetRepository extends CosmosRepository<Pet, String> {

	/**
	 * Retrieve pets by owner ID.
	 * @param ownerId Value to search for
	 * @return a Collection of matching {@link Pet}s
	 */
	List<Pet> findByOwnerId(String ownerId);

}
