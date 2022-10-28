package ar.edu.um.isa.petclinic.web.rest;

import ar.edu.um.isa.petclinic.repository.VisitsRepository;
import ar.edu.um.isa.petclinic.service.VisitsService;
import ar.edu.um.isa.petclinic.service.dto.VisitsDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.petclinic.domain.Visits}.
 */
@RestController
@RequestMapping("/api")
public class VisitsResource {

    private final Logger log = LoggerFactory.getLogger(VisitsResource.class);

    private static final String ENTITY_NAME = "visits";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisitsService visitsService;

    private final VisitsRepository visitsRepository;

    public VisitsResource(VisitsService visitsService, VisitsRepository visitsRepository) {
        this.visitsService = visitsService;
        this.visitsRepository = visitsRepository;
    }

    /**
     * {@code POST  /visits} : Create a new visits.
     *
     * @param visitsDTO the visitsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visitsDTO, or with status {@code 400 (Bad Request)} if the visits has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/visits")
    public ResponseEntity<VisitsDTO> createVisits(@Valid @RequestBody VisitsDTO visitsDTO) throws URISyntaxException {
        log.debug("REST request to save Visits : {}", visitsDTO);
        if (visitsDTO.getId() != null) {
            throw new BadRequestAlertException("A new visits cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VisitsDTO result = visitsService.save(visitsDTO);
        return ResponseEntity
            .created(new URI("/api/visits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /visits/:id} : Updates an existing visits.
     *
     * @param id the id of the visitsDTO to save.
     * @param visitsDTO the visitsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitsDTO,
     * or with status {@code 400 (Bad Request)} if the visitsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visitsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/visits/{id}")
    public ResponseEntity<VisitsDTO> updateVisits(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VisitsDTO visitsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Visits : {}, {}", id, visitsDTO);
        if (visitsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visitsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VisitsDTO result = visitsService.update(visitsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /visits/:id} : Partial updates given fields of an existing visits, field will ignore if it is null
     *
     * @param id the id of the visitsDTO to save.
     * @param visitsDTO the visitsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitsDTO,
     * or with status {@code 400 (Bad Request)} if the visitsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the visitsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the visitsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/visits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VisitsDTO> partialUpdateVisits(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VisitsDTO visitsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Visits partially : {}, {}", id, visitsDTO);
        if (visitsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visitsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VisitsDTO> result = visitsService.partialUpdate(visitsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /visits} : get all the visits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visits in body.
     */
    @GetMapping("/visits")
    public ResponseEntity<List<VisitsDTO>> getAllVisits(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Visits");
        Page<VisitsDTO> page = visitsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /visits/:id} : get the "id" visits.
     *
     * @param id the id of the visitsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visitsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/visits/{id}")
    public ResponseEntity<VisitsDTO> getVisits(@PathVariable Long id) {
        log.debug("REST request to get Visits : {}", id);
        Optional<VisitsDTO> visitsDTO = visitsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(visitsDTO);
    }

    /**
     * {@code DELETE  /visits/:id} : delete the "id" visits.
     *
     * @param id the id of the visitsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/visits/{id}")
    public ResponseEntity<Void> deleteVisits(@PathVariable Long id) {
        log.debug("REST request to delete Visits : {}", id);
        visitsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
