package org.springframework.samples.petclinic.owner;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for <code>PetType</code> domain objects.
 *
 * @author Patrick Baumgartner
 */
@Repository
public interface PetTypeRepository extends CosmosRepository<PetType, String> {

	/**
	 * Retrieve all {@link PetType}s from the data store, ordered by name.
	 * @return a List of {@link PetType}s.
	 */
	default List<PetType> findPetTypes() {
		return StreamSupport.stream(findAll().spliterator(), false)
			.sorted(Comparator.comparing(PetType::getName))
			.collect(Collectors.toList());
	}

}
