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
package org.springframework.samples.petclinic.vet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.petclinic.model.Person;

import com.azure.spring.data.cosmos.core.mapping.Container;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 */
@Container(containerName = "vets")
public class Vet extends Person {

	private List<String> specialtyIds = new ArrayList<>();

	private List<String> specialtyNames = new ArrayList<>();

	@XmlElement
	public List<String> getSpecialtyNames() {
		return this.specialtyNames;
	}

	public void setSpecialtyNames(List<String> specialtyNames) {
		this.specialtyNames = specialtyNames;
	}

	public List<String> getSpecialtyIds() {
		return this.specialtyIds;
	}

	public void setSpecialtyIds(List<String> specialtyIds) {
		this.specialtyIds = specialtyIds;
	}

	public int getNrOfSpecialties() {
		return this.specialtyIds.size();
	}

	public void addSpecialty(String specialtyId, String specialtyName) {
		this.specialtyIds.add(specialtyId);
		this.specialtyNames.add(specialtyName);
	}

}
