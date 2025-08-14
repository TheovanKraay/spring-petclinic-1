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
package org.springframework.samples.petclinic.model;

import java.io.Serializable;
import java.util.UUID;

import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import jakarta.annotation.PostConstruct;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects
 * needing this property.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class BaseEntity implements Serializable {

	@Id
	private String id;

	@PartitionKey
	private String partitionKey;

	public BaseEntity() {
		this.partitionKey = getClass().getSimpleName().toLowerCase();
		// Generate ID if not already set
		if (this.id == null) {
			this.id = generateId();
		}
	}

	/**
	 * Generate a unique ID for new entities
	 */
	private String generateId() {
		String entityType = getClass().getSimpleName().toLowerCase();
		return entityType + "-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Ensure this entity has an ID before saving
	 */
	public void ensureId() {
		if (this.id == null || this.id.trim().isEmpty()) {
			this.id = generateId();
		}
	}

	public String getPartitionKey() {
		return partitionKey;
	}

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

	public boolean isNew() {
		return this.id == null;
	}

}
