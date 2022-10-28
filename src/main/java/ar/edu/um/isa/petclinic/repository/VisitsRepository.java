package ar.edu.um.isa.petclinic.repository;

import ar.edu.um.isa.petclinic.domain.Visits;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Visits entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitsRepository extends JpaRepository<Visits, Long> {}
