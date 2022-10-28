package ar.edu.um.isa.petclinic.repository;

import ar.edu.um.isa.petclinic.domain.Vet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VetRepository extends JpaRepository<Vet, Long> {}
