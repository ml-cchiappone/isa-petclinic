package ar.edu.um.isa.petclinic.service;

import ar.edu.um.isa.petclinic.service.dto.HumanDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.isa.petclinic.domain.Human}.
 */
public interface HumanService {
    /**
     * Save a human.
     *
     * @param humanDTO the entity to save.
     * @return the persisted entity.
     */
    HumanDTO save(HumanDTO humanDTO);

    /**
     * Updates a human.
     *
     * @param humanDTO the entity to update.
     * @return the persisted entity.
     */
    HumanDTO update(HumanDTO humanDTO);

    /**
     * Partially updates a human.
     *
     * @param humanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HumanDTO> partialUpdate(HumanDTO humanDTO);

    /**
     * Get all the humans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HumanDTO> findAll(Pageable pageable);

    /**
     * Get the "id" human.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HumanDTO> findOne(Long id);

    /**
     * Delete the "id" human.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
