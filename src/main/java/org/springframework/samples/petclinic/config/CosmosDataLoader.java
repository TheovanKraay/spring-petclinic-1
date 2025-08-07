package org.springframework.samples.petclinic.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@Profile("cosmos")
public class CosmosDataLoader implements CommandLineRunner {

	private final OwnerRepository ownerRepository;

	private final PetTypeRepository petTypeRepository;

	private final VetRepository vetRepository;

	public CosmosDataLoader(OwnerRepository ownerRepository, PetTypeRepository petTypeRepository,
			VetRepository vetRepository) {
		this.ownerRepository = ownerRepository;
		this.petTypeRepository = petTypeRepository;
		this.vetRepository = vetRepository;
	}

	@Override
	public void run(String... args) {
		// Pet Types
		PetType dog = new PetType();
		dog.setId("1");
		dog.setName("dog");
		PetType cat = new PetType();
		cat.setId("2");
		cat.setName("cat");
		PetType bird = new PetType();
		bird.setId("3");
		bird.setName("bird");
		PetType hamster = new PetType();
		hamster.setId("4");
		hamster.setName("hamster");
		petTypeRepository.saveAll(Arrays.asList(dog, cat, bird, hamster));

		// Owners & Pets
		Owner owner1 = new Owner();
		owner1.setId("1");
		owner1.setFirstName("George");
		owner1.setLastName("Franklin");
		owner1.setAddress("110 W. Liberty St.");
		owner1.setCity("Madison");
		owner1.setTelephone("6085551023");
		Pet pet1 = new Pet();
		pet1.setId("1");
		pet1.setName("Max");
		pet1.setType(dog);
		pet1.setBirthDate(LocalDate.of(2010, 9, 7));
		owner1.addPet(pet1);
		ownerRepository.save(owner1);

		Owner owner2 = new Owner();
		owner2.setId("2");
		owner2.setFirstName("Betty");
		owner2.setLastName("Davis");
		owner2.setAddress("638 Cardinal Ave.");
		owner2.setCity("Sun Prairie");
		owner2.setTelephone("6085551749");
		Pet pet2 = new Pet();
		pet2.setId("2");
		pet2.setName("Lucky");
		pet2.setType(cat);
		pet2.setBirthDate(LocalDate.of(2012, 8, 6));
		owner2.addPet(pet2);
		ownerRepository.save(owner2);

		// Vets & Specialties
		Specialty radiology = new Specialty();
		radiology.setId("1");
		radiology.setName("radiology");
		Specialty surgery = new Specialty();
		surgery.setId("2");
		surgery.setName("surgery");
		Specialty dentistry = new Specialty();
		dentistry.setId("3");
		dentistry.setName("dentistry");

		Vet vet1 = new Vet();
		vet1.setId("1");
		vet1.setFirstName("James");
		vet1.setLastName("Carter");
		vetRepository.save(vet1);

		Vet vet2 = new Vet();
		vet2.setId("2");
		vet2.setFirstName("Helen");
		vet2.setLastName("Leary");
		vet2.addSpecialty(radiology);
		vetRepository.save(vet2);

		Vet vet3 = new Vet();
		vet3.setId("3");
		vet3.setFirstName("Linda");
		vet3.setLastName("Douglas");
		vet3.addSpecialty(surgery);
		vet3.addSpecialty(dentistry);
		vetRepository.save(vet3);
	}

}
