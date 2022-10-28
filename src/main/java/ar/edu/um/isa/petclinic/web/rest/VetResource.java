package ar.edu.um.isa.petclinic.web.rest;

import ar.edu.um.isa.petclinic.repository.VetRepository;
import ar.edu.um.isa.petclinic.service.VetService;
import ar.edu.um.isa.petclinic.service.dto.VetDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.petclinic.domain.Vet}.
 */
@RestController
@RequestMapping("/api")
public class VetResource {

    private final Logger log = LoggerFactory.getLogger(VetResource.class);

    private static final String ENTITY_NAME = "vet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VetService vetService;

    private final VetRepository vetRepository;

    public VetResource(VetService vetService, VetRepository vetRepository) {
        this.vetService = vetService;
        this.vetRepository = vetRepository;
    }

    /**
     * {@code POST  /vets} : Create a new vet.
     *
     * @param vetDTO the vetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vetDTO, or with status {@code 400 (Bad Request)} if the vet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vets")
    public ResponseEntity<VetDTO> createVet(@Valid @RequestBody VetDTO vetDTO) throws URISyntaxException {
        log.debug("REST request to save Vet : {}", vetDTO);
        if (vetDTO.getId() != null) {
            throw new BadRequestAlertException("A new vet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VetDTO result = vetService.save(vetDTO);
        return ResponseEntity
            .created(new URI("/api/vets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vets/:id} : Updates an existing vet.
     *
     * @param id the id of the vetDTO to save.
     * @param vetDTO the vetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vetDTO,
     * or with status {@code 400 (Bad Request)} if the vetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vets/{id}")
    public ResponseEntity<VetDTO> updateVet(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody VetDTO vetDTO)
        throws URISyntaxException {
        log.debug("REST request to update Vet : {}, {}", id, vetDTO);
        if (vetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VetDTO result = vetService.update(vetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vets/:id} : Partial updates given fields of an existing vet, field will ignore if it is null
     *
     * @param id the id of the vetDTO to save.
     * @param vetDTO the vetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vetDTO,
     * or with status {@code 400 (Bad Request)} if the vetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VetDTO> partialUpdateVet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VetDTO vetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vet partially : {}, {}", id, vetDTO);
        if (vetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VetDTO> result = vetService.partialUpdate(vetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vets} : get all the vets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vets in body.
     */
    @GetMapping("/vets")
    public ResponseEntity<List<VetDTO>> getAllVets(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Vets");
        Page<VetDTO> page = vetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vets/:id} : get the "id" vet.
     *
     * @param id the id of the vetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vets/{id}")
    public ResponseEntity<VetDTO> getVet(@PathVariable Long id) {
        log.debug("REST request to get Vet : {}", id);
        Optional<VetDTO> vetDTO = vetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vetDTO);
    }

    /**
     * {@code DELETE  /vets/:id} : delete the "id" vet.
     *
     * @param id the id of the vetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vets/{id}")
    public ResponseEntity<Void> deleteVet(@PathVariable Long id) {
        log.debug("REST request to delete Vet : {}", id);
        vetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
