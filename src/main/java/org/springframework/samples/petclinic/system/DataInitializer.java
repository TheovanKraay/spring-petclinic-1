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

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.SpecialtyRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Database initializer for Cosmos DB
 *
 * @author Theovan Kraay
 */
@Component
public class DataInitializer {

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

	@PostConstruct
	public void initializeData() {
		// Check if data already exists
		if (ownerRepository.findAll().iterator().hasNext()) {
			return; // Data already exists
		}

		// Initialize Pet Types
		PetType dogType = new PetType();
		dogType.setName("dog");
		dogType = petTypeRepository.save(dogType);

		PetType catType = new PetType();
		catType.setName("cat");
		catType = petTypeRepository.save(catType);

		PetType birdType = new PetType();
		birdType.setName("bird");
		birdType = petTypeRepository.save(birdType);

		PetType hamsterType = new PetType();
		hamsterType.setName("hamster");
		hamsterType = petTypeRepository.save(hamsterType);

		PetType snakeType = new PetType();
		snakeType.setName("snake");
		snakeType = petTypeRepository.save(snakeType);

		PetType lizardType = new PetType();
		lizardType.setName("lizard");
		lizardType = petTypeRepository.save(lizardType);

		// Initialize Specialties
		Specialty radiologySpecialty = new Specialty();
		radiologySpecialty.setName("radiology");
		radiologySpecialty = specialtyRepository.save(radiologySpecialty);

		Specialty surgerySpecialty = new Specialty();
		surgerySpecialty.setName("surgery");
		surgerySpecialty = specialtyRepository.save(surgerySpecialty);

		Specialty dentistrySpecialty = new Specialty();
		dentistrySpecialty.setName("dentistry");
		dentistrySpecialty = specialtyRepository.save(dentistrySpecialty);

		// Initialize Veterinarians
		Vet vet1 = new Vet();
		vet1.setFirstName("James");
		vet1.setLastName("Carter");
		Set<Specialty> vet1Specialties = new HashSet<>();
		vet1Specialties.add(radiologySpecialty);
		vet1.setSpecialtiesInternal(vet1Specialties);
		vet1 = vetRepository.save(vet1);

		Vet vet2 = new Vet();
		vet2.setFirstName("Helen");
		vet2.setLastName("Leary");
		Set<Specialty> vet2Specialties = new HashSet<>();
		vet2Specialties.add(radiologySpecialty);
		vet2.setSpecialtiesInternal(vet2Specialties);
		vet2 = vetRepository.save(vet2);

		Vet vet3 = new Vet();
		vet3.setFirstName("Linda");
		vet3.setLastName("Douglas");
		Set<Specialty> vet3Specialties = new HashSet<>();
		vet3Specialties.add(surgerySpecialty);
		vet3Specialties.add(dentistrySpecialty);
		vet3.setSpecialtiesInternal(vet3Specialties);
		vet3 = vetRepository.save(vet3);

		Vet vet4 = new Vet();
		vet4.setFirstName("Rafael");
		vet4.setLastName("Ortega");
		Set<Specialty> vet4Specialties = new HashSet<>();
		vet4Specialties.add(surgerySpecialty);
		vet4.setSpecialtiesInternal(vet4Specialties);
		vet4 = vetRepository.save(vet4);

		Vet vet5 = new Vet();
		vet5.setFirstName("Henry");
		vet5.setLastName("Stevens");
		Set<Specialty> vet5Specialties = new HashSet<>();
		vet5Specialties.add(radiologySpecialty);
		vet5.setSpecialtiesInternal(vet5Specialties);
		vet5 = vetRepository.save(vet5);

		Vet vet6 = new Vet();
		vet6.setFirstName("Sharon");
		vet6.setLastName("Jenkins");
		// No specialties for this vet
		vet6.setSpecialtiesInternal(new HashSet<>());
		vet6 = vetRepository.save(vet6);

		// Initialize Owners and Pets
		Owner owner1 = new Owner();
		owner1.setFirstName("George");
		owner1.setLastName("Franklin");
		owner1.setAddress("110 W. Liberty St.");
		owner1.setCity("Madison");
		owner1.setTelephone("6085551023");
		owner1 = ownerRepository.save(owner1);

		Pet pet1 = new Pet();
		pet1.setName("Leo");
		pet1.setBirthDate(LocalDate.of(2010, 9, 7));
		pet1.setType(catType);
		pet1.setOwnerId(owner1.getId());
		pet1 = petRepository.save(pet1);

		Owner owner2 = new Owner();
		owner2.setFirstName("Betty");
		owner2.setLastName("Davis");
		owner2.setAddress("638 Cardinal Ave.");
		owner2.setCity("Sun Prairie");
		owner2.setTelephone("6085551749");
		owner2 = ownerRepository.save(owner2);

		Pet pet2 = new Pet();
		pet2.setName("Basil");
		pet2.setBirthDate(LocalDate.of(2012, 8, 6));
		pet2.setType(hamsterType);
		pet2.setOwnerId(owner2.getId());
		pet2 = petRepository.save(pet2);

		Owner owner3 = new Owner();
		owner3.setFirstName("Eduardo");
		owner3.setLastName("Rodriquez");
		owner3.setAddress("2693 Commerce St.");
		owner3.setCity("McFarland");
		owner3.setTelephone("6085558763");
		owner3 = ownerRepository.save(owner3);

		Pet pet3 = new Pet();
		pet3.setName("Rosy");
		pet3.setBirthDate(LocalDate.of(2011, 4, 17));
		pet3.setType(dogType);
		pet3.setOwnerId(owner3.getId());
		pet3 = petRepository.save(pet3);

		Pet pet4 = new Pet();
		pet4.setName("Jewel");
		pet4.setBirthDate(LocalDate.of(2010, 3, 7));
		pet4.setType(dogType);
		pet4.setOwnerId(owner3.getId());
		pet4 = petRepository.save(pet4);

		Owner owner4 = new Owner();
		owner4.setFirstName("Harold");
		owner4.setLastName("Davis");
		owner4.setAddress("563 Friendly St.");
		owner4.setCity("Windsor");
		owner4.setTelephone("6085553198");
		owner4 = ownerRepository.save(owner4);

		Pet pet5 = new Pet();
		pet5.setName("Iggy");
		pet5.setBirthDate(LocalDate.of(2010, 11, 30));
		pet5.setType(lizardType);
		pet5.setOwnerId(owner4.getId());
		pet5 = petRepository.save(pet5);

		Owner owner5 = new Owner();
		owner5.setFirstName("Peter");
		owner5.setLastName("McTavish");
		owner5.setAddress("2387 S. Fair Way");
		owner5.setCity("Madison");
		owner5.setTelephone("6085552765");
		owner5 = ownerRepository.save(owner5);

		Pet pet6 = new Pet();
		pet6.setName("George");
		pet6.setBirthDate(LocalDate.of(2010, 1, 20));
		pet6.setType(snakeType);
		pet6.setOwnerId(owner5.getId());
		pet6 = petRepository.save(pet6);

		Owner owner6 = new Owner();
		owner6.setFirstName("Jean");
		owner6.setLastName("Coleman");
		owner6.setAddress("105 N. Lake St.");
		owner6.setCity("Monona");
		owner6.setTelephone("6085552654");
		owner6 = ownerRepository.save(owner6);

		Pet pet7 = new Pet();
		pet7.setName("Samantha");
		pet7.setBirthDate(LocalDate.of(2012, 9, 4));
		pet7.setType(catType);
		pet7.setOwnerId(owner6.getId());
		pet7 = petRepository.save(pet7);

		Pet pet8 = new Pet();
		pet8.setName("Max");
		pet8.setBirthDate(LocalDate.of(2012, 9, 4));
		pet8.setType(catType);
		pet8.setOwnerId(owner6.getId());
		pet8 = petRepository.save(pet8);

		// Initialize Visits
		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.of(2013, 1, 1));
		visit1.setDescription("rabies shot");
		visit1.setPetId(pet7.getId());
		visitRepository.save(visit1);

		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2013, 1, 2));
		visit2.setDescription("rabies shot");
		visit2.setPetId(pet8.getId());
		visitRepository.save(visit2);

		Visit visit3 = new Visit();
		visit3.setDate(LocalDate.of(2013, 1, 3));
		visit3.setDescription("neutered");
		visit3.setPetId(pet8.getId());
		visitRepository.save(visit3);

		Visit visit4 = new Visit();
		visit4.setDate(LocalDate.of(2013, 1, 4));
		visit4.setDescription("spayed");
		visit4.setPetId(pet7.getId());
		visitRepository.save(visit4);

		System.out.println("Sample data initialized in Cosmos DB");
	}

}
