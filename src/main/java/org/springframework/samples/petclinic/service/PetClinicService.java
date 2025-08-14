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
package org.springframework.samples.petclinic.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.vet.*;

/**
 * Service class for handling business logic and managing relationships between entities.
 */
@Service
public class PetClinicService {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private PetTypeRepository petTypeRepository;

	@Autowired
	private VetRepository vetRepository;

	@Autowired
	private SpecialtyRepository specialtyRepository;

	@Autowired
	private VisitRepository visitRepository;

	public Optional<Owner> findOwnerWithPets(String ownerId) {
		Optional<Owner> ownerOpt = ownerRepository.findById(ownerId);
		if (ownerOpt.isPresent()) {
			Owner owner = ownerOpt.get();
			// Load pets for this owner and populate their transient properties
			List<Pet> pets = petRepository.findByOwnerId(ownerId);
			for (Pet pet : pets) {
				populatePetTransientProperties(pet);
			}
			owner.setPets(pets);
			return Optional.of(owner);
		}
		return Optional.empty();
	}

	/**
	 * Populates transient properties for template compatibility
	 */
	private void populatePetTransientProperties(Pet pet) {
		// Populate type from typeId
		if (pet.getTypeId() != null) {
			Optional<PetType> typeOpt = petTypeRepository.findById(pet.getTypeId());
			typeOpt.ifPresent(pet::setType);
		}

		// Populate visits from visitIds
		if (pet.getVisitIds() != null && !pet.getVisitIds().isEmpty()) {
			List<Visit> visits = new ArrayList<>();
			for (String visitId : pet.getVisitIds()) {
				Optional<Visit> visitOpt = visitRepository.findById(visitId);
				visitOpt.ifPresent(visits::add);
			}
			pet.setVisits(visits);
		}
	}

	public List<Pet> findPetsByOwner(String ownerId) {
		return petRepository.findByOwnerId(ownerId);
	}

	public Optional<Pet> findPetWithVisits(String petId) {
		Optional<Pet> petOpt = petRepository.findById(petId);
		if (petOpt.isPresent()) {
			Pet pet = petOpt.get();
			// Load visits for this pet
			List<Visit> visits = visitRepository.findByPetId(petId);
			// For display purposes, populate visit IDs if not already there
			return Optional.of(pet);
		}
		return Optional.empty();
	}

	public List<Visit> findVisitsByPet(String petId) {
		return visitRepository.findByPetId(petId);
	}

	public Pet savePet(Pet pet, String ownerId) {
		pet.setOwnerId(ownerId);
		Pet savedPet = petRepository.save(pet);

		// Update owner's pet list
		Optional<Owner> ownerOpt = ownerRepository.findById(ownerId);
		if (ownerOpt.isPresent()) {
			Owner owner = ownerOpt.get();
			if (!owner.getPetIds().contains(savedPet.getId())) {
				owner.addPetId(savedPet.getId());
				ownerRepository.save(owner);
			}
		}

		return savedPet;
	}

	public Visit saveVisit(Visit visit, String petId) {
		visit.setPetId(petId);
		Visit savedVisit = visitRepository.save(visit);

		// Update pet's visit list
		Optional<Pet> petOpt = petRepository.findById(petId);
		if (petOpt.isPresent()) {
			Pet pet = petOpt.get();
			if (!pet.getVisitIds().contains(savedVisit.getId())) {
				pet.addVisitId(savedVisit.getId());
				petRepository.save(pet);
			}
		}

		return savedVisit;
	}

}
