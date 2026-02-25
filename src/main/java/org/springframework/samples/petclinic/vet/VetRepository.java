package org.springframework.samples.petclinic.vet;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for <code>Vet</code> domain objects.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Repository
public interface VetRepository extends CosmosRepository<Vet, String> {

}
