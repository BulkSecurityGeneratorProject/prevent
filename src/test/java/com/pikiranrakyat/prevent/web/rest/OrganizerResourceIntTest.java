package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.Organizer;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.repository.OrganizerRepository;
import com.pikiranrakyat.prevent.service.OrganizerService;
import com.pikiranrakyat.prevent.repository.search.OrganizerSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrganizerResource REST controller.
 *
 * @see OrganizerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class OrganizerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBB";

    private static final String DEFAULT_TWITTER = "AAAAA";
    private static final String UPDATED_TWITTER = "BBBBB";

    @Inject
    private OrganizerRepository organizerRepository;

    @Inject
    private OrganizerService organizerService;

    @Inject
    private OrganizerSearchRepository organizerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrganizerMockMvc;

    private Organizer organizer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganizerResource organizerResource = new OrganizerResource();
        ReflectionTestUtils.setField(organizerResource, "organizerService", organizerService);
        this.restOrganizerMockMvc = MockMvcBuilders.standaloneSetup(organizerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organizer createEntity(EntityManager em) {
        Organizer organizer = new Organizer()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .facebook(DEFAULT_FACEBOOK)
                .twitter(DEFAULT_TWITTER);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        organizer.setUser(user);
        return organizer;
    }

    @Before
    public void initTest() {
        organizerSearchRepository.deleteAll();
        organizer = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizer() throws Exception {
        int databaseSizeBeforeCreate = organizerRepository.findAll().size();

        // Create the Organizer

        restOrganizerMockMvc.perform(post("/api/organizers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizer)))
                .andExpect(status().isCreated());

        // Validate the Organizer in the database
        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeCreate + 1);
        Organizer testOrganizer = organizers.get(organizers.size() - 1);
        assertThat(testOrganizer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganizer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrganizer.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testOrganizer.getTwitter()).isEqualTo(DEFAULT_TWITTER);

        // Validate the Organizer in ElasticSearch
        Organizer organizerEs = organizerSearchRepository.findOne(testOrganizer.getId());
        assertThat(organizerEs).isEqualToComparingFieldByField(testOrganizer);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizerRepository.findAll().size();
        // set the field null
        organizer.setName(null);

        // Create the Organizer, which fails.

        restOrganizerMockMvc.perform(post("/api/organizers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizer)))
                .andExpect(status().isBadRequest());

        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizerRepository.findAll().size();
        // set the field null
        organizer.setDescription(null);

        // Create the Organizer, which fails.

        restOrganizerMockMvc.perform(post("/api/organizers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizer)))
                .andExpect(status().isBadRequest());

        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizers() throws Exception {
        // Initialize the database
        organizerRepository.saveAndFlush(organizer);

        // Get all the organizers
        restOrganizerMockMvc.perform(get("/api/organizers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(organizer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
                .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER.toString())));
    }

    @Test
    @Transactional
    public void getOrganizer() throws Exception {
        // Initialize the database
        organizerRepository.saveAndFlush(organizer);

        // Get the organizer
        restOrganizerMockMvc.perform(get("/api/organizers/{id}", organizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organizer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK.toString()))
            .andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizer() throws Exception {
        // Get the organizer
        restOrganizerMockMvc.perform(get("/api/organizers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizer() throws Exception {
        // Initialize the database
        organizerService.save(organizer);

        int databaseSizeBeforeUpdate = organizerRepository.findAll().size();

        // Update the organizer
        Organizer updatedOrganizer = organizerRepository.findOne(organizer.getId());
        updatedOrganizer
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .facebook(UPDATED_FACEBOOK)
                .twitter(UPDATED_TWITTER);

        restOrganizerMockMvc.perform(put("/api/organizers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrganizer)))
                .andExpect(status().isOk());

        // Validate the Organizer in the database
        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeUpdate);
        Organizer testOrganizer = organizers.get(organizers.size() - 1);
        assertThat(testOrganizer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganizer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrganizer.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testOrganizer.getTwitter()).isEqualTo(UPDATED_TWITTER);

        // Validate the Organizer in ElasticSearch
        Organizer organizerEs = organizerSearchRepository.findOne(testOrganizer.getId());
        assertThat(organizerEs).isEqualToComparingFieldByField(testOrganizer);
    }

    @Test
    @Transactional
    public void deleteOrganizer() throws Exception {
        // Initialize the database
        organizerService.save(organizer);

        int databaseSizeBeforeDelete = organizerRepository.findAll().size();

        // Get the organizer
        restOrganizerMockMvc.perform(delete("/api/organizers/{id}", organizer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean organizerExistsInEs = organizerSearchRepository.exists(organizer.getId());
        assertThat(organizerExistsInEs).isFalse();

        // Validate the database is empty
        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrganizer() throws Exception {
        // Initialize the database
        organizerService.save(organizer);

        // Search the organizer
        restOrganizerMockMvc.perform(get("/api/_search/organizers?query=id:" + organizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER.toString())));
    }
}
