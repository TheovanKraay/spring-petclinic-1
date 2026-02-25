package org.springframework.samples.petclinic.owner;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.samples.petclinic.model.NamedEntity;

/**
 * @author Juergen Hoeller Can be Cat, Dog, Hamster...
 */
@Container(containerName = "petTypes")
public class PetType extends NamedEntity {

	@PartitionKey
	private String partitionKey = "petType";

	public String getPartitionKey() {
		return partitionKey;
	}

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

}
