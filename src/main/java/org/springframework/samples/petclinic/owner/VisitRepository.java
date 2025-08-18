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
import com.azure.spring.data.cosmos.repository.Query;

/**
 * Repository class for <code>Visit</code> domain objects.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface VisitRepository extends CosmosRepository<Visit, String> {

	/**
	 * Retrieve all {@link Visit}s by pet ID.
	 * @param petId the pet ID
	 * @return a Collection of {@link Visit}s.
	 */
	@Query("SELECT * FROM visits v WHERE v.petId = @petId ORDER BY v.date DESC")
	List<Visit> findByPetId(String petId);

}
