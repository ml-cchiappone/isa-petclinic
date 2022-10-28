package ar.edu.um.isa.petclinic.repository;

import ar.edu.um.isa.petclinic.domain.Pets;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {}
