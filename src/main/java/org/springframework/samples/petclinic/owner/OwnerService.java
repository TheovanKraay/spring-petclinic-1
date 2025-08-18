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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for Owner-related operations.
 *
 * @author Theovan Kraay
 */
@Service
public class OwnerService {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private VisitRepository visitRepository;

	public List<Owner> findByLastNameStartingWith(String lastName) {
		List<Owner> owners = ownerRepository.findByLastNameStartingWith(lastName);

		// Load pets for each owner
		for (Owner owner : owners) {
			List<Pet> pets = petRepository.findByOwnerId(owner.getId());

			// Load visits for each pet
			for (Pet pet : pets) {
				List<Visit> visits = visitRepository.findByPetId(pet.getId());
				pet.getVisits().clear();
				pet.getVisits().addAll(visits);
			}

			owner.getPets().clear();
			owner.getPets().addAll(pets);
		}

		return owners;
	}

	public Optional<Owner> findById(String id) {
		Optional<Owner> ownerOpt = ownerRepository.findById(id);

		if (ownerOpt.isPresent()) {
			Owner owner = ownerOpt.get();
			List<Pet> pets = petRepository.findByOwnerId(owner.getId());

			// Load visits for each pet
			for (Pet pet : pets) {
				List<Visit> visits = visitRepository.findByPetId(pet.getId());
				pet.getVisits().clear();
				pet.getVisits().addAll(visits);
			}

			owner.getPets().clear();
			owner.getPets().addAll(pets);
		}

		return ownerOpt;
	}

	public Owner save(Owner owner) {
		return ownerRepository.save(owner);
	}

}
