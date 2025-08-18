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

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for Pet-related operations.
 *
 * @author Theovan Kraay
 */
@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private OwnerService ownerService;

	public Pet save(Pet pet) {
		return petRepository.save(pet);
	}

	public Optional<Pet> findById(String id) {
		return petRepository.findById(id);
	}

	public Pet savePetForOwner(String ownerId, Pet pet) {
		pet.setOwnerId(ownerId);
		Pet savedPet = petRepository.save(pet);

		// Update the owner's pet list
		Optional<Owner> ownerOpt = ownerService.findById(ownerId);
		if (ownerOpt.isPresent()) {
			Owner owner = ownerOpt.get();
			// Remove existing pet if updating
			owner.getPets()
				.removeIf(existingPet -> existingPet.getId() != null && existingPet.getId().equals(savedPet.getId()));
			// Add updated pet
			owner.getPets().add(savedPet);
		}

		return savedPet;
	}

}
