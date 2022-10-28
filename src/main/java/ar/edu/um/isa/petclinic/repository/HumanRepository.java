package ar.edu.um.isa.petclinic.repository;

import ar.edu.um.isa.petclinic.domain.Human;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Human entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {}
