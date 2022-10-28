package ar.edu.um.isa.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.petclinic.IntegrationTest;
import ar.edu.um.isa.petclinic.domain.Visits;
import ar.edu.um.isa.petclinic.repository.VisitsRepository;
import ar.edu.um.isa.petclinic.service.dto.VisitsDTO;
import ar.edu.um.isa.petclinic.service.mapper.VisitsMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VisitsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VisitsResourceIT {

    private static final Instant DEFAULT_VISITDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VISITDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/visits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VisitsRepository visitsRepository;

    @Autowired
    private VisitsMapper visitsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisitsMockMvc;

    private Visits visits;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visits createEntity(EntityManager em) {
        Visits visits = new Visits().visitdate(DEFAULT_VISITDATE).description(DEFAULT_DESCRIPTION);
        return visits;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visits createUpdatedEntity(EntityManager em) {
        Visits visits = new Visits().visitdate(UPDATED_VISITDATE).description(UPDATED_DESCRIPTION);
        return visits;
    }

    @BeforeEach
    public void initTest() {
        visits = createEntity(em);
    }

    @Test
    @Transactional
    void createVisits() throws Exception {
        int databaseSizeBeforeCreate = visitsRepository.findAll().size();
        // Create the Visits
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);
        restVisitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitsDTO)))
            .andExpect(status().isCreated());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeCreate + 1);
        Visits testVisits = visitsList.get(visitsList.size() - 1);
        assertThat(testVisits.getVisitdate()).isEqualTo(DEFAULT_VISITDATE);
        assertThat(testVisits.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createVisitsWithExistingId() throws Exception {
        // Create the Visits with an existing ID
        visits.setId(1L);
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        int databaseSizeBeforeCreate = visitsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVisitdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitsRepository.findAll().size();
        // set the field null
        visits.setVisitdate(null);

        // Create the Visits, which fails.
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        restVisitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitsDTO)))
            .andExpect(status().isBadRequest());

        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitsRepository.findAll().size();
        // set the field null
        visits.setDescription(null);

        // Create the Visits, which fails.
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        restVisitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitsDTO)))
            .andExpect(status().isBadRequest());

        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVisits() throws Exception {
        // Initialize the database
        visitsRepository.saveAndFlush(visits);

        // Get all the visitsList
        restVisitsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visits.getId().intValue())))
            .andExpect(jsonPath("$.[*].visitdate").value(hasItem(DEFAULT_VISITDATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getVisits() throws Exception {
        // Initialize the database
        visitsRepository.saveAndFlush(visits);

        // Get the visits
        restVisitsMockMvc
            .perform(get(ENTITY_API_URL_ID, visits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visits.getId().intValue()))
            .andExpect(jsonPath("$.visitdate").value(DEFAULT_VISITDATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingVisits() throws Exception {
        // Get the visits
        restVisitsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVisits() throws Exception {
        // Initialize the database
        visitsRepository.saveAndFlush(visits);

        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();

        // Update the visits
        Visits updatedVisits = visitsRepository.findById(visits.getId()).get();
        // Disconnect from session so that the updates on updatedVisits are not directly saved in db
        em.detach(updatedVisits);
        updatedVisits.visitdate(UPDATED_VISITDATE).description(UPDATED_DESCRIPTION);
        VisitsDTO visitsDTO = visitsMapper.toDto(updatedVisits);

        restVisitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visitsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visitsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
        Visits testVisits = visitsList.get(visitsList.size() - 1);
        assertThat(testVisits.getVisitdate()).isEqualTo(UPDATED_VISITDATE);
        assertThat(testVisits.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingVisits() throws Exception {
        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();
        visits.setId(count.incrementAndGet());

        // Create the Visits
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visitsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisits() throws Exception {
        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();
        visits.setId(count.incrementAndGet());

        // Create the Visits
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisits() throws Exception {
        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();
        visits.setId(count.incrementAndGet());

        // Create the Visits
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVisitsWithPatch() throws Exception {
        // Initialize the database
        visitsRepository.saveAndFlush(visits);

        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();

        // Update the visits using partial update
        Visits partialUpdatedVisits = new Visits();
        partialUpdatedVisits.setId(visits.getId());

        partialUpdatedVisits.description(UPDATED_DESCRIPTION);

        restVisitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisits.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisits))
            )
            .andExpect(status().isOk());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
        Visits testVisits = visitsList.get(visitsList.size() - 1);
        assertThat(testVisits.getVisitdate()).isEqualTo(DEFAULT_VISITDATE);
        assertThat(testVisits.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateVisitsWithPatch() throws Exception {
        // Initialize the database
        visitsRepository.saveAndFlush(visits);

        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();

        // Update the visits using partial update
        Visits partialUpdatedVisits = new Visits();
        partialUpdatedVisits.setId(visits.getId());

        partialUpdatedVisits.visitdate(UPDATED_VISITDATE).description(UPDATED_DESCRIPTION);

        restVisitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisits.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisits))
            )
            .andExpect(status().isOk());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
        Visits testVisits = visitsList.get(visitsList.size() - 1);
        assertThat(testVisits.getVisitdate()).isEqualTo(UPDATED_VISITDATE);
        assertThat(testVisits.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingVisits() throws Exception {
        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();
        visits.setId(count.incrementAndGet());

        // Create the Visits
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visitsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisits() throws Exception {
        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();
        visits.setId(count.incrementAndGet());

        // Create the Visits
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisits() throws Exception {
        int databaseSizeBeforeUpdate = visitsRepository.findAll().size();
        visits.setId(count.incrementAndGet());

        // Create the Visits
        VisitsDTO visitsDTO = visitsMapper.toDto(visits);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(visitsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visits in the database
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVisits() throws Exception {
        // Initialize the database
        visitsRepository.saveAndFlush(visits);

        int databaseSizeBeforeDelete = visitsRepository.findAll().size();

        // Delete the visits
        restVisitsMockMvc
            .perform(delete(ENTITY_API_URL_ID, visits.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Visits> visitsList = visitsRepository.findAll();
        assertThat(visitsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
