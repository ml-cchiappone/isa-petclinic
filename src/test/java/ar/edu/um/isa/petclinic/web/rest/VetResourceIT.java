package ar.edu.um.isa.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.petclinic.IntegrationTest;
import ar.edu.um.isa.petclinic.domain.Vet;
import ar.edu.um.isa.petclinic.repository.VetRepository;
import ar.edu.um.isa.petclinic.service.dto.VetDTO;
import ar.edu.um.isa.petclinic.service.mapper.VetMapper;
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
 * Integration tests for the {@link VetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VetResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private VetMapper vetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVetMockMvc;

    private Vet vet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vet createEntity(EntityManager em) {
        Vet vet = new Vet().firstname(DEFAULT_FIRSTNAME).lastname(DEFAULT_LASTNAME);
        return vet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vet createUpdatedEntity(EntityManager em) {
        Vet vet = new Vet().firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);
        return vet;
    }

    @BeforeEach
    public void initTest() {
        vet = createEntity(em);
    }

    @Test
    @Transactional
    void createVet() throws Exception {
        int databaseSizeBeforeCreate = vetRepository.findAll().size();
        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);
        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isCreated());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeCreate + 1);
        Vet testVet = vetList.get(vetList.size() - 1);
        assertThat(testVet.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testVet.getLastname()).isEqualTo(DEFAULT_LASTNAME);
    }

    @Test
    @Transactional
    void createVetWithExistingId() throws Exception {
        // Create the Vet with an existing ID
        vet.setId(1L);
        VetDTO vetDTO = vetMapper.toDto(vet);

        int databaseSizeBeforeCreate = vetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vetRepository.findAll().size();
        // set the field null
        vet.setFirstname(null);

        // Create the Vet, which fails.
        VetDTO vetDTO = vetMapper.toDto(vet);

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vetRepository.findAll().size();
        // set the field null
        vet.setLastname(null);

        // Create the Vet, which fails.
        VetDTO vetDTO = vetMapper.toDto(vet);

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVets() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vet.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)));
    }

    @Test
    @Transactional
    void getVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get the vet
        restVetMockMvc
            .perform(get(ENTITY_API_URL_ID, vet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vet.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME));
    }

    @Test
    @Transactional
    void getNonExistingVet() throws Exception {
        // Get the vet
        restVetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        int databaseSizeBeforeUpdate = vetRepository.findAll().size();

        // Update the vet
        Vet updatedVet = vetRepository.findById(vet.getId()).get();
        // Disconnect from session so that the updates on updatedVet are not directly saved in db
        em.detach(updatedVet);
        updatedVet.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);
        VetDTO vetDTO = vetMapper.toDto(updatedVet);

        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
        Vet testVet = vetList.get(vetList.size() - 1);
        assertThat(testVet.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testVet.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void putNonExistingVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVetWithPatch() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        int databaseSizeBeforeUpdate = vetRepository.findAll().size();

        // Update the vet using partial update
        Vet partialUpdatedVet = new Vet();
        partialUpdatedVet.setId(vet.getId());

        partialUpdatedVet.lastname(UPDATED_LASTNAME);

        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
        Vet testVet = vetList.get(vetList.size() - 1);
        assertThat(testVet.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testVet.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void fullUpdateVetWithPatch() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        int databaseSizeBeforeUpdate = vetRepository.findAll().size();

        // Update the vet using partial update
        Vet partialUpdatedVet = new Vet();
        partialUpdatedVet.setId(vet.getId());

        partialUpdatedVet.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);

        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
        Vet testVet = vetList.get(vetList.size() - 1);
        assertThat(testVet.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testVet.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void patchNonExistingVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        int databaseSizeBeforeDelete = vetRepository.findAll().size();

        // Delete the vet
        restVetMockMvc.perform(delete(ENTITY_API_URL_ID, vet.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
