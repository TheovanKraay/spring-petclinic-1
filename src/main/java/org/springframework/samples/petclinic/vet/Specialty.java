package org.springframework.samples.petclinic.vet;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.samples.petclinic.model.NamedEntity;

/**
 * Models a {@link Vet Vet's} specialty (for example, dentistry).
 *
 * @author Juergen Hoeller
 */
@Container(containerName = "specialties")
public class Specialty extends NamedEntity {

	@PartitionKey
	private String partitionKey = "specialty";

	public String getPartitionKey() {
		return partitionKey;
	}

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

}
