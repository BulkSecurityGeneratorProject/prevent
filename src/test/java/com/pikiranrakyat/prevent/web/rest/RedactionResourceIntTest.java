package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;
import com.pikiranrakyat.prevent.domain.Redaction;
import com.pikiranrakyat.prevent.repository.RedactionRepository;
import com.pikiranrakyat.prevent.repository.search.RedactionSearchRepository;
import com.pikiranrakyat.prevent.service.RedactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RedactionResource REST controller.
 *
 * @see RedactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class RedactionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";


    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private RedactionRepository redactionRepository;

    @Inject
    private RedactionService redactionService;

    @Inject
    private RedactionSearchRepository redactionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRedactionMockMvc;

    private Redaction redaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RedactionResource redactionResource = new RedactionResource();
        ReflectionTestUtils.setField(redactionResource, "redactionService", redactionService);
        this.restRedactionMockMvc = MockMvcBuilders.standaloneSetup(redactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Redaction createEntity(EntityManager em) {
        Redaction redaction = new Redaction()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION);
        return redaction;
    }

    @Before
    public void initTest() {
        redactionSearchRepository.deleteAll();
        redaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createRedaction() throws Exception {
        int databaseSizeBeforeCreate = redactionRepository.findAll().size();

        // Create the Redaction

        restRedactionMockMvc.perform(post("/api/redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(redaction)))
                .andExpect(status().isCreated());

        // Validate the Redaction in the database
        List<Redaction> redactions = redactionRepository.findAll();
        assertThat(redactions).hasSize(databaseSizeBeforeCreate + 1);
        Redaction testRedaction = redactions.get(redactions.size() - 1);
        assertThat(testRedaction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRedaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Redaction in ElasticSearch
        Redaction redactionEs = redactionSearchRepository.findOne(testRedaction.getId());
        assertThat(redactionEs).isEqualToComparingFieldByField(testRedaction);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = redactionRepository.findAll().size();
        // set the field null
        redaction.setName(null);

        // Create the Redaction, which fails.

        restRedactionMockMvc.perform(post("/api/redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(redaction)))
                .andExpect(status().isBadRequest());

        List<Redaction> redactions = redactionRepository.findAll();
        assertThat(redactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRedactions() throws Exception {
        // Initialize the database
        redactionRepository.saveAndFlush(redaction);

        // Get all the redactions
        restRedactionMockMvc.perform(get("/api/redactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(redaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getRedaction() throws Exception {
        // Initialize the database
        redactionRepository.saveAndFlush(redaction);

        // Get the redaction
        restRedactionMockMvc.perform(get("/api/redactions/{id}", redaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(redaction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRedaction() throws Exception {
        // Get the redaction
        restRedactionMockMvc.perform(get("/api/redactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRedaction() throws Exception {
        // Initialize the database
        redactionService.save(redaction);

        int databaseSizeBeforeUpdate = redactionRepository.findAll().size();

        // Update the redaction
        Redaction updatedRedaction = redactionRepository.findOne(redaction.getId());
        updatedRedaction
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION);

        restRedactionMockMvc.perform(put("/api/redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRedaction)))
                .andExpect(status().isOk());

        // Validate the Redaction in the database
        List<Redaction> redactions = redactionRepository.findAll();
        assertThat(redactions).hasSize(databaseSizeBeforeUpdate);
        Redaction testRedaction = redactions.get(redactions.size() - 1);
        assertThat(testRedaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRedaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Redaction in ElasticSearch
        Redaction redactionEs = redactionSearchRepository.findOne(testRedaction.getId());
        assertThat(redactionEs).isEqualToComparingFieldByField(testRedaction);
    }

    @Test
    @Transactional
    public void deleteRedaction() throws Exception {
        // Initialize the database
        redactionService.save(redaction);

        int databaseSizeBeforeDelete = redactionRepository.findAll().size();

        // Get the redaction
        restRedactionMockMvc.perform(delete("/api/redactions/{id}", redaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean redactionExistsInEs = redactionSearchRepository.exists(redaction.getId());
        assertThat(redactionExistsInEs).isFalse();

        // Validate the database is empty
        List<Redaction> redactions = redactionRepository.findAll();
        assertThat(redactions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRedaction() throws Exception {
        // Initialize the database
        redactionService.save(redaction);

        // Search the redaction
        restRedactionMockMvc.perform(get("/api/_search/redactions?query=id:" + redaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
