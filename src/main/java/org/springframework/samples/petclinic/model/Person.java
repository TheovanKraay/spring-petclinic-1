package org.springframework.samples.petclinic.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Simple JavaBean domain object representing a person.
 *
 * @author Ken Krebs
 */
public class Person extends BaseEntity {

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
