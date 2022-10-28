package ar.edu.um.isa.petclinic.repository;

import ar.edu.um.isa.petclinic.domain.Types;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Types entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypesRepository extends JpaRepository<Types, Long> {}
