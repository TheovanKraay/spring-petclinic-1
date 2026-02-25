package org.springframework.samples.petclinic.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Simple JavaBean domain object adds a name property to <code>BaseEntity</code>. Used as
 * a base class for objects needing these properties.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Wick Dynex
 */
public class NamedEntity extends BaseEntity {

	@NotBlank
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}
