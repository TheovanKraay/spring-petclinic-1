package org.springframework.samples.petclinic.vet;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for <code>Specialty</code> domain objects.
 */
@Repository
public interface SpecialtyRepository extends CosmosRepository<Specialty, String> {

}
