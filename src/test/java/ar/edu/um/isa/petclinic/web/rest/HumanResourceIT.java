package ar.edu.um.isa.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.petclinic.IntegrationTest;
import ar.edu.um.isa.petclinic.domain.Human;
import ar.edu.um.isa.petclinic.repository.HumanRepository;
import ar.edu.um.isa.petclinic.service.dto.HumanDTO;
import ar.edu.um.isa.petclinic.service.mapper.HumanMapper;
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
 * Integration tests for the {@link HumanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HumanResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/humans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HumanRepository humanRepository;

    @Autowired
    private HumanMapper humanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHumanMockMvc;

    private Human human;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Human createEntity(EntityManager em) {
        Human human = new Human()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .telephone(DEFAULT_TELEPHONE);
        return human;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Human createUpdatedEntity(EntityManager em) {
        Human human = new Human()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);
        return human;
    }

    @BeforeEach
    public void initTest() {
        human = createEntity(em);
    }

    @Test
    @Transactional
    void createHuman() throws Exception {
        int databaseSizeBeforeCreate = humanRepository.findAll().size();
        // Create the Human
        HumanDTO humanDTO = humanMapper.toDto(human);
        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanDTO)))
            .andExpect(status().isCreated());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeCreate + 1);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testHuman.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testHuman.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHuman.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testHuman.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void createHumanWithExistingId() throws Exception {
        // Create the Human with an existing ID
        human.setId(1L);
        HumanDTO humanDTO = humanMapper.toDto(human);

        int databaseSizeBeforeCreate = humanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanRepository.findAll().size();
        // set the field null
        human.setFirstname(null);

        // Create the Human, which fails.
        HumanDTO humanDTO = humanMapper.toDto(human);

        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanDTO)))
            .andExpect(status().isBadRequest());

        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanRepository.findAll().size();
        // set the field null
        human.setLastname(null);

        // Create the Human, which fails.
        HumanDTO humanDTO = humanMapper.toDto(human);

        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanDTO)))
            .andExpect(status().isBadRequest());

        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanRepository.findAll().size();
        // set the field null
        human.setAddress(null);

        // Create the Human, which fails.
        HumanDTO humanDTO = humanMapper.toDto(human);

        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanDTO)))
            .andExpect(status().isBadRequest());

        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanRepository.findAll().size();
        // set the field null
        human.setTelephone(null);

        // Create the Human, which fails.
        HumanDTO humanDTO = humanMapper.toDto(human);

        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanDTO)))
            .andExpect(status().isBadRequest());

        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHumans() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        // Get all the humanList
        restHumanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(human.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getHuman() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        // Get the human
        restHumanMockMvc
            .perform(get(ENTITY_API_URL_ID, human.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(human.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getNonExistingHuman() throws Exception {
        // Get the human
        restHumanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHuman() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        int databaseSizeBeforeUpdate = humanRepository.findAll().size();

        // Update the human
        Human updatedHuman = humanRepository.findById(human.getId()).get();
        // Disconnect from session so that the updates on updatedHuman are not directly saved in db
        em.detach(updatedHuman);
        updatedHuman
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);
        HumanDTO humanDTO = humanMapper.toDto(updatedHuman);

        restHumanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, humanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(humanDTO))
            )
            .andExpect(status().isOk());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testHuman.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testHuman.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHuman.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testHuman.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void putNonExistingHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // Create the Human
        HumanDTO humanDTO = humanMapper.toDto(human);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, humanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(humanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // Create the Human
        HumanDTO humanDTO = humanMapper.toDto(human);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(humanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // Create the Human
        HumanDTO humanDTO = humanMapper.toDto(human);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHumanWithPatch() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        int databaseSizeBeforeUpdate = humanRepository.findAll().size();

        // Update the human using partial update
        Human partialUpdatedHuman = new Human();
        partialUpdatedHuman.setId(human.getId());

        partialUpdatedHuman.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).telephone(UPDATED_TELEPHONE);

        restHumanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuman.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuman))
            )
            .andExpect(status().isOk());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testHuman.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testHuman.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHuman.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testHuman.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void fullUpdateHumanWithPatch() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        int databaseSizeBeforeUpdate = humanRepository.findAll().size();

        // Update the human using partial update
        Human partialUpdatedHuman = new Human();
        partialUpdatedHuman.setId(human.getId());

        partialUpdatedHuman
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);

        restHumanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuman.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuman))
            )
            .andExpect(status().isOk());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testHuman.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testHuman.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHuman.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testHuman.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void patchNonExistingHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // Create the Human
        HumanDTO humanDTO = humanMapper.toDto(human);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, humanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(humanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // Create the Human
        HumanDTO humanDTO = humanMapper.toDto(human);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(humanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // Create the Human
        HumanDTO humanDTO = humanMapper.toDto(human);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(humanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHuman() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        int databaseSizeBeforeDelete = humanRepository.findAll().size();

        // Delete the human
        restHumanMockMvc
            .perform(delete(ENTITY_API_URL_ID, human.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
