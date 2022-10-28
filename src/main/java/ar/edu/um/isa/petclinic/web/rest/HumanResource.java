package ar.edu.um.isa.petclinic.web.rest;

import ar.edu.um.isa.petclinic.repository.HumanRepository;
import ar.edu.um.isa.petclinic.service.HumanService;
import ar.edu.um.isa.petclinic.service.dto.HumanDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.petclinic.domain.Human}.
 */
@RestController
@RequestMapping("/api")
public class HumanResource {

    private final Logger log = LoggerFactory.getLogger(HumanResource.class);

    private static final String ENTITY_NAME = "human";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HumanService humanService;

    private final HumanRepository humanRepository;

    public HumanResource(HumanService humanService, HumanRepository humanRepository) {
        this.humanService = humanService;
        this.humanRepository = humanRepository;
    }

    /**
     * {@code POST  /humans} : Create a new human.
     *
     * @param humanDTO the humanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new humanDTO, or with status {@code 400 (Bad Request)} if the human has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/humans")
    public ResponseEntity<HumanDTO> createHuman(@Valid @RequestBody HumanDTO humanDTO) throws URISyntaxException {
        log.debug("REST request to save Human : {}", humanDTO);
        if (humanDTO.getId() != null) {
            throw new BadRequestAlertException("A new human cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HumanDTO result = humanService.save(humanDTO);
        return ResponseEntity
            .created(new URI("/api/humans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /humans/:id} : Updates an existing human.
     *
     * @param id the id of the humanDTO to save.
     * @param humanDTO the humanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humanDTO,
     * or with status {@code 400 (Bad Request)} if the humanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the humanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/humans/{id}")
    public ResponseEntity<HumanDTO> updateHuman(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HumanDTO humanDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Human : {}, {}", id, humanDTO);
        if (humanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!humanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HumanDTO result = humanService.update(humanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, humanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /humans/:id} : Partial updates given fields of an existing human, field will ignore if it is null
     *
     * @param id the id of the humanDTO to save.
     * @param humanDTO the humanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humanDTO,
     * or with status {@code 400 (Bad Request)} if the humanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the humanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the humanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/humans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HumanDTO> partialUpdateHuman(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HumanDTO humanDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Human partially : {}, {}", id, humanDTO);
        if (humanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!humanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HumanDTO> result = humanService.partialUpdate(humanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, humanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /humans} : get all the humans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of humans in body.
     */
    @GetMapping("/humans")
    public ResponseEntity<List<HumanDTO>> getAllHumans(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Humans");
        Page<HumanDTO> page = humanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /humans/:id} : get the "id" human.
     *
     * @param id the id of the humanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the humanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/humans/{id}")
    public ResponseEntity<HumanDTO> getHuman(@PathVariable Long id) {
        log.debug("REST request to get Human : {}", id);
        Optional<HumanDTO> humanDTO = humanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(humanDTO);
    }

    /**
     * {@code DELETE  /humans/:id} : delete the "id" human.
     *
     * @param id the id of the humanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/humans/{id}")
    public ResponseEntity<Void> deleteHuman(@PathVariable Long id) {
        log.debug("REST request to delete Human : {}", id);
        humanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
