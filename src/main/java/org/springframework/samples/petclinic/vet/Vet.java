package org.springframework.samples.petclinic.vet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.model.Person;

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

	@PartitionKey
	private String partitionKey = "vet";

	private List<String> specialtyIds = new ArrayList<>();

	@JsonIgnore
	private transient List<Specialty> specialties = new ArrayList<>();

	public String getPartitionKey() {
		return partitionKey;
	}

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

	public List<String> getSpecialtyIds() {
		return specialtyIds;
	}

	public void setSpecialtyIds(List<String> specialtyIds) {
		this.specialtyIds = specialtyIds;
	}

	@XmlElement
	public List<Specialty> getSpecialties() {
		return this.specialties.stream()
			.sorted(Comparator.comparing(NamedEntity::getName))
			.collect(Collectors.toList());
	}

	public void setSpecialties(List<Specialty> specialties) {
		this.specialties = specialties;
	}

	public int getNrOfSpecialties() {
		return this.specialties.size();
	}

	public void addSpecialty(Specialty specialty) {
		this.specialties.add(specialty);
		if (specialty.getId() != null) {
			this.specialtyIds.add(specialty.getId());
		}
	}

}
