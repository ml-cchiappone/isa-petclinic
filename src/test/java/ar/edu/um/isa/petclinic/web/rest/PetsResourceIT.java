package ar.edu.um.isa.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.petclinic.IntegrationTest;
import ar.edu.um.isa.petclinic.domain.Pets;
import ar.edu.um.isa.petclinic.repository.PetsRepository;
import ar.edu.um.isa.petclinic.service.dto.PetsDTO;
import ar.edu.um.isa.petclinic.service.mapper.PetsMapper;
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
 * Integration tests for the {@link PetsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PetsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PetsRepository petsRepository;

    @Autowired
    private PetsMapper petsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetsMockMvc;

    private Pets pets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pets createEntity(EntityManager em) {
        Pets pets = new Pets().name(DEFAULT_NAME);
        return pets;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pets createUpdatedEntity(EntityManager em) {
        Pets pets = new Pets().name(UPDATED_NAME);
        return pets;
    }

    @BeforeEach
    public void initTest() {
        pets = createEntity(em);
    }

    @Test
    @Transactional
    void createPets() throws Exception {
        int databaseSizeBeforeCreate = petsRepository.findAll().size();
        // Create the Pets
        PetsDTO petsDTO = petsMapper.toDto(pets);
        restPetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petsDTO)))
            .andExpect(status().isCreated());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeCreate + 1);
        Pets testPets = petsList.get(petsList.size() - 1);
        assertThat(testPets.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPetsWithExistingId() throws Exception {
        // Create the Pets with an existing ID
        pets.setId(1L);
        PetsDTO petsDTO = petsMapper.toDto(pets);

        int databaseSizeBeforeCreate = petsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = petsRepository.findAll().size();
        // set the field null
        pets.setName(null);

        // Create the Pets, which fails.
        PetsDTO petsDTO = petsMapper.toDto(pets);

        restPetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petsDTO)))
            .andExpect(status().isBadRequest());

        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPets() throws Exception {
        // Initialize the database
        petsRepository.saveAndFlush(pets);

        // Get all the petsList
        restPetsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pets.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPets() throws Exception {
        // Initialize the database
        petsRepository.saveAndFlush(pets);

        // Get the pets
        restPetsMockMvc
            .perform(get(ENTITY_API_URL_ID, pets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pets.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPets() throws Exception {
        // Get the pets
        restPetsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPets() throws Exception {
        // Initialize the database
        petsRepository.saveAndFlush(pets);

        int databaseSizeBeforeUpdate = petsRepository.findAll().size();

        // Update the pets
        Pets updatedPets = petsRepository.findById(pets.getId()).get();
        // Disconnect from session so that the updates on updatedPets are not directly saved in db
        em.detach(updatedPets);
        updatedPets.name(UPDATED_NAME);
        PetsDTO petsDTO = petsMapper.toDto(updatedPets);

        restPetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
        Pets testPets = petsList.get(petsList.size() - 1);
        assertThat(testPets.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPets() throws Exception {
        int databaseSizeBeforeUpdate = petsRepository.findAll().size();
        pets.setId(count.incrementAndGet());

        // Create the Pets
        PetsDTO petsDTO = petsMapper.toDto(pets);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPets() throws Exception {
        int databaseSizeBeforeUpdate = petsRepository.findAll().size();
        pets.setId(count.incrementAndGet());

        // Create the Pets
        PetsDTO petsDTO = petsMapper.toDto(pets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPets() throws Exception {
        int databaseSizeBeforeUpdate = petsRepository.findAll().size();
        pets.setId(count.incrementAndGet());

        // Create the Pets
        PetsDTO petsDTO = petsMapper.toDto(pets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePetsWithPatch() throws Exception {
        // Initialize the database
        petsRepository.saveAndFlush(pets);

        int databaseSizeBeforeUpdate = petsRepository.findAll().size();

        // Update the pets using partial update
        Pets partialUpdatedPets = new Pets();
        partialUpdatedPets.setId(pets.getId());

        restPetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPets))
            )
            .andExpect(status().isOk());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
        Pets testPets = petsList.get(petsList.size() - 1);
        assertThat(testPets.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePetsWithPatch() throws Exception {
        // Initialize the database
        petsRepository.saveAndFlush(pets);

        int databaseSizeBeforeUpdate = petsRepository.findAll().size();

        // Update the pets using partial update
        Pets partialUpdatedPets = new Pets();
        partialUpdatedPets.setId(pets.getId());

        partialUpdatedPets.name(UPDATED_NAME);

        restPetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPets))
            )
            .andExpect(status().isOk());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
        Pets testPets = petsList.get(petsList.size() - 1);
        assertThat(testPets.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPets() throws Exception {
        int databaseSizeBeforeUpdate = petsRepository.findAll().size();
        pets.setId(count.incrementAndGet());

        // Create the Pets
        PetsDTO petsDTO = petsMapper.toDto(pets);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, petsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPets() throws Exception {
        int databaseSizeBeforeUpdate = petsRepository.findAll().size();
        pets.setId(count.incrementAndGet());

        // Create the Pets
        PetsDTO petsDTO = petsMapper.toDto(pets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPets() throws Exception {
        int databaseSizeBeforeUpdate = petsRepository.findAll().size();
        pets.setId(count.incrementAndGet());

        // Create the Pets
        PetsDTO petsDTO = petsMapper.toDto(pets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(petsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pets in the database
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePets() throws Exception {
        // Initialize the database
        petsRepository.saveAndFlush(pets);

        int databaseSizeBeforeDelete = petsRepository.findAll().size();

        // Delete the pets
        restPetsMockMvc
            .perform(delete(ENTITY_API_URL_ID, pets.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pets> petsList = petsRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
