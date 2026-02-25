package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for <code>Owner</code> domain objects.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Wick Dynex
 */
@Repository
public interface OwnerRepository extends CosmosRepository<Owner, String> {

	/**
	 * Retrieve {@link Owner}s from the data store by last name, returning all owners
	 * whose last name <i>starts</i> with the given name.
	 * @param lastName Value to search for
	 * @return a List of matching {@link Owner}s (or an empty List if none found)
	 */
	default List<Owner> findByLastNameStartingWith(String lastName) {
		return StreamSupport.stream(findAll().spliterator(), false)
			.filter(o -> o.getLastName() != null && o.getLastName().toLowerCase().startsWith(lastName.toLowerCase()))
			.collect(Collectors.toList());
	}

}
