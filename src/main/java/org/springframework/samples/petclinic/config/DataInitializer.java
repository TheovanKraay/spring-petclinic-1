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
package org.springframework.samples.petclinic.config;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.SpecialtyRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.samples.petclinic.owner.VisitRepository;

/**
 * Data seeding component that populates the Cosmos DB with sample PetClinic data. This
 * runs automatically when the application starts and only adds data if the database is
 * empty.
 *
 * @author Copilot Assistant
 */
@Component
public class DataInitializer implements CommandLineRunner {

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

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Starting data initialization...");

		// Create pet types if they don't exist
		if (petTypeRepository.count() == 0) {
			System.out.println("Creating pet types...");
			createPetTypes();
		}
		else {
			System.out.println("Pet types already exist. Skipping pet type creation.");
		}

		// Create specialties if they don't exist
		if (specialtyRepository.count() == 0) {
			System.out.println("Creating specialties...");
			createSpecialties();
		}
		else {
			System.out.println("Specialties already exist. Skipping specialty creation.");
		}

		// Create veterinarians if they don't exist
		if (vetRepository.count() == 0) {
			System.out.println("Creating veterinarians...");
			createVeterinarians();
		}
		else {
			System.out.println("Veterinarians already exist. Skipping veterinarian creation.");
		}

		// Create owners if they don't exist
		if (ownerRepository.count() == 0) {
			System.out.println("Creating owners...");
			createOwners();

			// Only create pets and visits if we're creating owners
			System.out.println("Creating pets...");
			createPets();

			System.out.println("Creating visits...");
			createVisits();
		}
		else {
			System.out.println("Owners already exist. Skipping owner, pet, and visit creation.");
		}

		System.out.println("Data seeding completed successfully!");
	}

	private void createPetTypes() {
		System.out.println("Creating pet types...");

		PetType cat = new PetType();
		cat.setId("pettype-1");
		cat.setName("cat");
		petTypeRepository.save(cat);

		PetType dog = new PetType();
		dog.setId("pettype-2");
		dog.setName("dog");
		petTypeRepository.save(dog);

		PetType lizard = new PetType();
		lizard.setId("pettype-3");
		lizard.setName("lizard");
		petTypeRepository.save(lizard);

		PetType snake = new PetType();
		snake.setId("pettype-4");
		snake.setName("snake");
		petTypeRepository.save(snake);

		PetType bird = new PetType();
		bird.setId("pettype-5");
		bird.setName("bird");
		petTypeRepository.save(bird);

		PetType hamster = new PetType();
		hamster.setId("pettype-6");
		hamster.setName("hamster");
		petTypeRepository.save(hamster);

		System.out.println("Pet types created successfully.");
	}

	private void createSpecialties() {
		System.out.println("Creating veterinary specialties...");

		Specialty radiology = new Specialty();
		radiology.setId("specialty-1");
		radiology.setName("radiology");
		specialtyRepository.save(radiology);

		Specialty surgery = new Specialty();
		surgery.setId("specialty-2");
		surgery.setName("surgery");
		specialtyRepository.save(surgery);

		Specialty dentistry = new Specialty();
		dentistry.setId("specialty-3");
		dentistry.setName("dentistry");
		specialtyRepository.save(dentistry);

		System.out.println("Veterinary specialties created successfully.");
	}

	private void createVeterinarians() {
		System.out.println("Creating veterinarians...");

		// James Carter
		Vet vet1 = new Vet();
		vet1.setId("vet-1");
		vet1.setFirstName("James");
		vet1.setLastName("Carter");
		vet1.setSpecialtyIds(new ArrayList<>());
		vetRepository.save(vet1);

		// Helen Leary - radiology
		Vet vet2 = new Vet();
		vet2.setId("vet-2");
		vet2.setFirstName("Helen");
		vet2.setLastName("Leary");
		vet2.setSpecialtyIds(new ArrayList<>());
		vet2.getSpecialtyIds().add("specialty-1"); // radiology
		vetRepository.save(vet2);

		// Linda Douglas - surgery, dentistry
		Vet vet3 = new Vet();
		vet3.setId("vet-3");
		vet3.setFirstName("Linda");
		vet3.setLastName("Douglas");
		vet3.setSpecialtyIds(new ArrayList<>());
		vet3.getSpecialtyIds().add("specialty-2"); // surgery
		vet3.getSpecialtyIds().add("specialty-3"); // dentistry
		vetRepository.save(vet3);

		// Rafael Ortega - surgery
		Vet vet4 = new Vet();
		vet4.setId("vet-4");
		vet4.setFirstName("Rafael");
		vet4.setLastName("Ortega");
		vet4.setSpecialtyIds(new ArrayList<>());
		vet4.getSpecialtyIds().add("specialty-2"); // surgery
		vetRepository.save(vet4);

		// Henry Stevens - radiology
		Vet vet5 = new Vet();
		vet5.setId("vet-5");
		vet5.setFirstName("Henry");
		vet5.setLastName("Stevens");
		vet5.setSpecialtyIds(new ArrayList<>());
		vet5.getSpecialtyIds().add("specialty-1"); // radiology
		vetRepository.save(vet5);

		// Sharon Jenkins
		Vet vet6 = new Vet();
		vet6.setId("vet-6");
		vet6.setFirstName("Sharon");
		vet6.setLastName("Jenkins");
		vet6.setSpecialtyIds(new ArrayList<>());
		vetRepository.save(vet6);

		System.out.println("Veterinarians created successfully.");
	}

	private void createOwners() {
		System.out.println("Creating pet owners...");

		// George Franklin
		Owner owner1 = new Owner();
		owner1.setId("owner-1");
		owner1.setFirstName("George");
		owner1.setLastName("Franklin");
		owner1.setAddress("110 W. Liberty St.");
		owner1.setCity("Madison");
		owner1.setTelephone("6085551023");
		ownerRepository.save(owner1);

		// Betty Davis
		Owner owner2 = new Owner();
		owner2.setId("owner-2");
		owner2.setFirstName("Betty");
		owner2.setLastName("Davis");
		owner2.setAddress("638 Cardinal Ave.");
		owner2.setCity("Sun Prairie");
		owner2.setTelephone("6085551749");
		ownerRepository.save(owner2);

		// Eduardo Rodriquez
		Owner owner3 = new Owner();
		owner3.setId("owner-3");
		owner3.setFirstName("Eduardo");
		owner3.setLastName("Rodriquez");
		owner3.setAddress("2693 Commerce St.");
		owner3.setCity("McFarland");
		owner3.setTelephone("6085558763");
		ownerRepository.save(owner3);

		// Harold Davis
		Owner owner4 = new Owner();
		owner4.setId("owner-4");
		owner4.setFirstName("Harold");
		owner4.setLastName("Davis");
		owner4.setAddress("563 Friendly St.");
		owner4.setCity("Windsor");
		owner4.setTelephone("6085553198");
		ownerRepository.save(owner4);

		// Peter McTavish
		Owner owner5 = new Owner();
		owner5.setId("owner-5");
		owner5.setFirstName("Peter");
		owner5.setLastName("McTavish");
		owner5.setAddress("2387 S. Fair Way");
		owner5.setCity("Madison");
		owner5.setTelephone("6085552765");
		ownerRepository.save(owner5);

		// Jean Coleman
		Owner owner6 = new Owner();
		owner6.setId("owner-6");
		owner6.setFirstName("Jean");
		owner6.setLastName("Coleman");
		owner6.setAddress("105 N. Lake St.");
		owner6.setCity("Monona");
		owner6.setTelephone("6085552654");
		ownerRepository.save(owner6);

		// Jeff Black
		Owner owner7 = new Owner();
		owner7.setId("owner-7");
		owner7.setFirstName("Jeff");
		owner7.setLastName("Black");
		owner7.setAddress("1450 Oak Blvd.");
		owner7.setCity("Monona");
		owner7.setTelephone("6085555387");
		ownerRepository.save(owner7);

		// Maria Escobito
		Owner owner8 = new Owner();
		owner8.setId("owner-8");
		owner8.setFirstName("Maria");
		owner8.setLastName("Escobito");
		owner8.setAddress("345 Maple St.");
		owner8.setCity("Madison");
		owner8.setTelephone("6085557683");
		ownerRepository.save(owner8);

		// David Schroeder
		Owner owner9 = new Owner();
		owner9.setId("owner-9");
		owner9.setFirstName("David");
		owner9.setLastName("Schroeder");
		owner9.setAddress("2749 Blackhawk Trail");
		owner9.setCity("Madison");
		owner9.setTelephone("6085559435");
		ownerRepository.save(owner9);

		// Carlos Estaban
		Owner owner10 = new Owner();
		owner10.setId("owner-10");
		owner10.setFirstName("Carlos");
		owner10.setLastName("Estaban");
		owner10.setAddress("2335 Independence La.");
		owner10.setCity("Waunakee");
		owner10.setTelephone("6085555487");
		ownerRepository.save(owner10);

		System.out.println("Pet owners created successfully.");
	}

	private void createPets() {
		System.out.println("Creating pets...");

		// Leo (George Franklin's cat)
		Pet pet1 = new Pet();
		pet1.setId("pet-1");
		pet1.setName("Leo");
		pet1.setBirthDate(LocalDate.of(2010, 9, 7));
		pet1.setTypeId("pettype-1"); // cat
		pet1.setOwnerId("owner-1");
		petRepository.save(pet1);

		// Basil (Betty Davis's hamster)
		Pet pet2 = new Pet();
		pet2.setId("pet-2");
		pet2.setName("Basil");
		pet2.setBirthDate(LocalDate.of(2012, 8, 6));
		pet2.setTypeId("pettype-6"); // hamster
		pet2.setOwnerId("owner-2");
		petRepository.save(pet2);

		// Rosy (Eduardo Rodriquez's dog)
		Pet pet3 = new Pet();
		pet3.setId("pet-3");
		pet3.setName("Rosy");
		pet3.setBirthDate(LocalDate.of(2011, 4, 17));
		pet3.setTypeId("pettype-2"); // dog
		pet3.setOwnerId("owner-3");
		petRepository.save(pet3);

		// Jewel (Eduardo Rodriquez's dog)
		Pet pet4 = new Pet();
		pet4.setId("pet-4");
		pet4.setName("Jewel");
		pet4.setBirthDate(LocalDate.of(2010, 3, 7));
		pet4.setTypeId("pettype-2"); // dog
		pet4.setOwnerId("owner-3");
		petRepository.save(pet4);

		// Iggy (Harold Davis's lizard)
		Pet pet5 = new Pet();
		pet5.setId("pet-5");
		pet5.setName("Iggy");
		pet5.setBirthDate(LocalDate.of(2010, 11, 30));
		pet5.setTypeId("pettype-3"); // lizard
		pet5.setOwnerId("owner-4");
		petRepository.save(pet5);

		// George (Peter McTavish's snake)
		Pet pet6 = new Pet();
		pet6.setId("pet-6");
		pet6.setName("George");
		pet6.setBirthDate(LocalDate.of(2010, 1, 20));
		pet6.setTypeId("pettype-4"); // snake
		pet6.setOwnerId("owner-5");
		petRepository.save(pet6);

		// Samantha (Jean Coleman's cat)
		Pet pet7 = new Pet();
		pet7.setId("pet-7");
		pet7.setName("Samantha");
		pet7.setBirthDate(LocalDate.of(2012, 9, 4));
		pet7.setTypeId("pettype-1"); // cat
		pet7.setOwnerId("owner-6");
		petRepository.save(pet7);

		// Max (Jean Coleman's cat)
		Pet pet8 = new Pet();
		pet8.setId("pet-8");
		pet8.setName("Max");
		pet8.setBirthDate(LocalDate.of(2012, 9, 4));
		pet8.setTypeId("pettype-1"); // cat
		pet8.setOwnerId("owner-6");
		petRepository.save(pet8);

		// Lucky (Jeff Black's bird)
		Pet pet9 = new Pet();
		pet9.setId("pet-9");
		pet9.setName("Lucky");
		pet9.setBirthDate(LocalDate.of(2011, 8, 6));
		pet9.setTypeId("pettype-5"); // bird
		pet9.setOwnerId("owner-7");
		petRepository.save(pet9);

		// Mulligan (Jeff Black's dog)
		Pet pet10 = new Pet();
		pet10.setId("pet-10");
		pet10.setName("Mulligan");
		pet10.setBirthDate(LocalDate.of(2007, 2, 24));
		pet10.setTypeId("pettype-2"); // dog
		pet10.setOwnerId("owner-7");
		petRepository.save(pet10);

		// Freddy (Maria Escobito's bird)
		Pet pet11 = new Pet();
		pet11.setId("pet-11");
		pet11.setName("Freddy");
		pet11.setBirthDate(LocalDate.of(2010, 3, 9));
		pet11.setTypeId("pettype-5"); // bird
		pet11.setOwnerId("owner-8");
		petRepository.save(pet11);

		// Lucky (David Schroeder's dog)
		Pet pet12 = new Pet();
		pet12.setId("pet-12");
		pet12.setName("Lucky");
		pet12.setBirthDate(LocalDate.of(2010, 6, 24));
		pet12.setTypeId("pettype-2"); // dog
		pet12.setOwnerId("owner-9");
		petRepository.save(pet12);

		// Sly (Carlos Estaban's cat)
		Pet pet13 = new Pet();
		pet13.setId("pet-13");
		pet13.setName("Sly");
		pet13.setBirthDate(LocalDate.of(2012, 6, 8));
		pet13.setTypeId("pettype-1"); // cat
		pet13.setOwnerId("owner-10");
		petRepository.save(pet13);

		System.out.println("Pets created successfully.");
	}

	private void createVisits() {
		System.out.println("Creating veterinary visits...");

		// Visit for Samantha (rabies shot)
		Visit visit1 = new Visit();
		visit1.setId("visit-1");
		visit1.setPetId("pet-7");
		visit1.setDate(LocalDate.of(2013, 1, 1));
		visit1.setDescription("rabies shot");
		visitRepository.save(visit1);

		// Visit for Max (rabies shot)
		Visit visit2 = new Visit();
		visit2.setId("visit-2");
		visit2.setPetId("pet-8");
		visit2.setDate(LocalDate.of(2013, 1, 2));
		visit2.setDescription("rabies shot");
		visitRepository.save(visit2);

		// Visit for Mulligan (neutered)
		Visit visit3 = new Visit();
		visit3.setId("visit-3");
		visit3.setPetId("pet-10");
		visit3.setDate(LocalDate.of(2013, 1, 3));
		visit3.setDescription("neutered");
		visitRepository.save(visit3);

		// Visit for Lucky (spayed)
		Visit visit4 = new Visit();
		visit4.setId("visit-4");
		visit4.setPetId("pet-12");
		visit4.setDate(LocalDate.of(2013, 1, 4));
		visit4.setDescription("spayed");
		visitRepository.save(visit4);

		System.out.println("Veterinary visits created successfully.");
	}

}
