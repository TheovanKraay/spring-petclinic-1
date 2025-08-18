package org.springframework.samples.petclinic.vet;

import org.springframework.samples.petclinic.model.NamedEntity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import jakarta.validation.constraints.NotBlank;

/**
 * Models a {@link Vet Vet's} specialty (for example, dentistry).
 *
 * @author Juergen Hoeller
 */
@Container(containerName = "specialties")
public class Specialty extends NamedEntity {

	@PartitionKey
	private String partitionKey;

	@Override
	@NotBlank
	public String getName() {
		return super.getName();
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		this.partitionKey = name; // Use name as partition key
	}

	public String getPartitionKey() {
		return partitionKey;
	}

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

}
