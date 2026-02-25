package org.springframework.samples.petclinic.config;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.SpecialtyRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

	private final PetTypeRepository petTypeRepository;

	private final SpecialtyRepository specialtyRepository;

	private final VetRepository vetRepository;

	private final OwnerRepository ownerRepository;

	public DataSeeder(PetTypeRepository petTypeRepository, SpecialtyRepository specialtyRepository,
			VetRepository vetRepository, OwnerRepository ownerRepository) {
		this.petTypeRepository = petTypeRepository;
		this.specialtyRepository = specialtyRepository;
		this.vetRepository = vetRepository;
		this.ownerRepository = ownerRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// Only seed if data doesn't already exist
		long existingOwners = StreamSupport.stream(ownerRepository.findAll().spliterator(), false).count();
		if (existingOwners > 0) {
			log.info("Data already exists, skipping seeding.");
			return;
		}

		log.info("Seeding data into Cosmos DB...");

		// Create pet types
		PetType cat = createPetType("cat");
		PetType dog = createPetType("dog");
		PetType lizard = createPetType("lizard");
		PetType snake = createPetType("snake");
		PetType bird = createPetType("bird");
		PetType hamster = createPetType("hamster");

		// Create specialties
		Specialty radiology = createSpecialty("radiology");
		Specialty surgery = createSpecialty("surgery");
		Specialty dentistry = createSpecialty("dentistry");

		// Create vets
		createVet("James", "Carter", List.of());
		createVet("Helen", "Leary", List.of(radiology));
		createVet("Linda", "Douglas", List.of(surgery, dentistry));
		createVet("Rafael", "Ortega", List.of(surgery));
		createVet("Henry", "Stevens", List.of(radiology));
		createVet("Sharon", "Jenkins", List.of());

		// Create owners with pets and visits
		Owner owner1 = createOwner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");
		addPet(owner1, "Leo", LocalDate.of(2010, 9, 7), cat);

		Owner owner2 = createOwner("Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749");
		addPet(owner2, "Basil", LocalDate.of(2012, 8, 6), hamster);

		Owner owner3 = createOwner("Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", "6085558763");
		addPet(owner3, "Rosy", LocalDate.of(2011, 4, 17), dog);
		addPet(owner3, "Jewel", LocalDate.of(2010, 3, 7), dog);

		Owner owner4 = createOwner("Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198");
		addPet(owner4, "Iggy", LocalDate.of(2010, 11, 30), lizard);

		Owner owner5 = createOwner("Peter", "McTavish", "2387 S. Fair Way", "Madison", "6085552765");
		addPet(owner5, "George", LocalDate.of(2010, 1, 20), snake);

		Owner owner6 = createOwner("Jean", "Coleman", "105 N. Lake St.", "Monona", "6085552654");
		Pet max = addPet(owner6, "Max", LocalDate.of(2012, 9, 4), cat);
		addVisit(owner6, max, LocalDate.of(2013, 1, 1), "rabies shot");
		addVisit(owner6, max, LocalDate.of(2013, 1, 4), "neutered");

		Owner owner7 = createOwner("Jeff", "Black", "1450 Oak Blvd.", "Monona", "6085555387");
		addPet(owner7, "Lucky", LocalDate.of(2011, 5, 5), dog);

		Owner owner8 = createOwner("Maria", "Escobito", "345 Maple St.", "Madison", "6085557683");
		addPet(owner8, "Mulligan", LocalDate.of(2007, 2, 24), dog);

		Owner owner9 = createOwner("David", "Schroeder", "2749 Blackhawk Trail", "Madison", "6085559435");
		addPet(owner9, "Freddy", LocalDate.of(2010, 3, 9), bird);

		Owner owner10 = createOwner("Carlos", "Estaban", "2335 Independence La.", "Waunakee", "6085555487");
		Pet lucky = addPet(owner10, "Lucky", LocalDate.of(2010, 6, 24), dog);
		Pet sly = addPet(owner10, "Sly", LocalDate.of(2012, 6, 8), cat);
		addVisit(owner10, lucky, LocalDate.of(2013, 1, 2), "rabies shot");
		addVisit(owner10, sly, LocalDate.of(2013, 1, 3), "neutered");

		log.info("Data seeding complete.");
	}

	private PetType createPetType(String name) {
		PetType petType = new PetType();
		petType.setName(name);
		return petTypeRepository.save(petType);
	}

	private Specialty createSpecialty(String name) {
		Specialty specialty = new Specialty();
		specialty.setName(name);
		return specialtyRepository.save(specialty);
	}

	private void createVet(String firstName, String lastName, List<Specialty> specialties) {
		Vet vet = new Vet();
		vet.setFirstName(firstName);
		vet.setLastName(lastName);
		for (Specialty s : specialties) {
			vet.addSpecialty(s);
		}
		vetRepository.save(vet);
	}

	private Owner createOwner(String firstName, String lastName, String address, String city, String telephone) {
		Owner owner = new Owner();
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		owner.setAddress(address);
		owner.setCity(city);
		owner.setTelephone(telephone);
		return ownerRepository.save(owner);
	}

	private Pet addPet(Owner owner, String name, LocalDate birthDate, PetType type) {
		Pet pet = new Pet();
		pet.setName(name);
		pet.setBirthDate(birthDate);
		pet.setType(type);
		owner.addPet(pet);
		ownerRepository.save(owner);
		return pet;
	}

	private void addVisit(Owner owner, Pet pet, LocalDate date, String description) {
		Visit visit = new Visit();
		visit.setDate(date);
		visit.setDescription(description);
		pet.addVisit(visit);
		ownerRepository.save(owner);
	}

}
