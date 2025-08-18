package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.model.NamedEntity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Juergen Hoeller Can be Cat, Dog, Hamster...
 */
@Container(containerName = "petTypes")
public class PetType extends NamedEntity {

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
