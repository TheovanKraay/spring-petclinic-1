package org.springframework.samples.petclinic.model;

import java.io.Serializable;

import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import org.springframework.data.annotation.Id;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects
 * needing this property.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class BaseEntity implements Serializable {

	@Id
	@GeneratedValue
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isNew() {
		return this.id == null;
	}

}
