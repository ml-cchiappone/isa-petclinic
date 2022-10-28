package ar.edu.um.isa.petclinic.web.rest;

import ar.edu.um.isa.petclinic.repository.PetsRepository;
import ar.edu.um.isa.petclinic.service.PetsService;
import ar.edu.um.isa.petclinic.service.dto.PetsDTO;
import ar.edu.um.isa.petclinic.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.petclinic.domain.Pets}.
 */
@RestController
@RequestMapping("/api")
public class PetsResource {

    private final Logger log = LoggerFactory.getLogger(PetsResource.class);

    private static final String ENTITY_NAME = "pets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PetsService petsService;

    private final PetsRepository petsRepository;

    public PetsResource(PetsService petsService, PetsRepository petsRepository) {
        this.petsService = petsService;
        this.petsRepository = petsRepository;
    }

    /**
     * {@code POST  /pets} : Create a new pets.
     *
     * @param petsDTO the petsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new petsDTO, or with status {@code 400 (Bad Request)} if the pets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pets")
    public ResponseEntity<PetsDTO> createPets(@Valid @RequestBody PetsDTO petsDTO) throws URISyntaxException {
        log.debug("REST request to save Pets : {}", petsDTO);
        if (petsDTO.getId() != null) {
            throw new BadRequestAlertException("A new pets cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PetsDTO result = petsService.save(petsDTO);
        return ResponseEntity
            .created(new URI("/api/pets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pets/:id} : Updates an existing pets.
     *
     * @param id the id of the petsDTO to save.
     * @param petsDTO the petsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated petsDTO,
     * or with status {@code 400 (Bad Request)} if the petsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the petsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pets/{id}")
    public ResponseEntity<PetsDTO> updatePets(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PetsDTO petsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pets : {}, {}", id, petsDTO);
        if (petsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, petsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PetsDTO result = petsService.update(petsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, petsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pets/:id} : Partial updates given fields of an existing pets, field will ignore if it is null
     *
     * @param id the id of the petsDTO to save.
     * @param petsDTO the petsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated petsDTO,
     * or with status {@code 400 (Bad Request)} if the petsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the petsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the petsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PetsDTO> partialUpdatePets(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PetsDTO petsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pets partially : {}, {}", id, petsDTO);
        if (petsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, petsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PetsDTO> result = petsService.partialUpdate(petsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, petsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pets} : get all the pets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pets in body.
     */
    @GetMapping("/pets")
    public ResponseEntity<List<PetsDTO>> getAllPets(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pets");
        Page<PetsDTO> page = petsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pets/:id} : get the "id" pets.
     *
     * @param id the id of the petsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the petsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pets/{id}")
    public ResponseEntity<PetsDTO> getPets(@PathVariable Long id) {
        log.debug("REST request to get Pets : {}", id);
        Optional<PetsDTO> petsDTO = petsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(petsDTO);
    }

    /**
     * {@code DELETE  /pets/:id} : delete the "id" pets.
     *
     * @param id the id of the petsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> deletePets(@PathVariable Long id) {
        log.debug("REST request to delete Pets : {}", id);
        petsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
