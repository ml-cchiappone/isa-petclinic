package ar.edu.um.isa.petclinic.service;

import ar.edu.um.isa.petclinic.service.dto.PetsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.isa.petclinic.domain.Pets}.
 */
public interface PetsService {
    /**
     * Save a pets.
     *
     * @param petsDTO the entity to save.
     * @return the persisted entity.
     */
    PetsDTO save(PetsDTO petsDTO);

    /**
     * Updates a pets.
     *
     * @param petsDTO the entity to update.
     * @return the persisted entity.
     */
    PetsDTO update(PetsDTO petsDTO);

    /**
     * Partially updates a pets.
     *
     * @param petsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PetsDTO> partialUpdate(PetsDTO petsDTO);

    /**
     * Get all the pets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PetsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pets.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PetsDTO> findOne(Long id);

    /**
     * Delete the "id" pets.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
