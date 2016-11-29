package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.Circulation;
import com.pikiranrakyat.prevent.repository.CirculationRepository;
import com.pikiranrakyat.prevent.service.CirculationService;
import com.pikiranrakyat.prevent.repository.search.CirculationSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CirculationResource REST controller.
 *
 * @see CirculationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class CirculationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private CirculationRepository circulationRepository;

    @Inject
    private CirculationService circulationService;

    @Inject
    private CirculationSearchRepository circulationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCirculationMockMvc;

    private Circulation circulation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CirculationResource circulationResource = new CirculationResource();
        ReflectionTestUtils.setField(circulationResource, "circulationService", circulationService);
        this.restCirculationMockMvc = MockMvcBuilders.standaloneSetup(circulationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Circulation createEntity(EntityManager em) {
        Circulation circulation = new Circulation()
                .name(DEFAULT_NAME)
                .price(DEFAULT_PRICE)
                .description(DEFAULT_DESCRIPTION);
        return circulation;
    }

    @Before
    public void initTest() {
        circulationSearchRepository.deleteAll();
        circulation = createEntity(em);
    }

    @Test
    @Transactional
    public void createCirculation() throws Exception {
        int databaseSizeBeforeCreate = circulationRepository.findAll().size();

        // Create the Circulation

        restCirculationMockMvc.perform(post("/api/circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circulation)))
                .andExpect(status().isCreated());

        // Validate the Circulation in the database
        List<Circulation> circulations = circulationRepository.findAll();
        assertThat(circulations).hasSize(databaseSizeBeforeCreate + 1);
        Circulation testCirculation = circulations.get(circulations.size() - 1);
        assertThat(testCirculation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCirculation.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCirculation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Circulation in ElasticSearch
        Circulation circulationEs = circulationSearchRepository.findOne(testCirculation.getId());
        assertThat(circulationEs).isEqualToComparingFieldByField(testCirculation);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = circulationRepository.findAll().size();
        // set the field null
        circulation.setName(null);

        // Create the Circulation, which fails.

        restCirculationMockMvc.perform(post("/api/circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circulation)))
                .andExpect(status().isBadRequest());

        List<Circulation> circulations = circulationRepository.findAll();
        assertThat(circulations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = circulationRepository.findAll().size();
        // set the field null
        circulation.setPrice(null);

        // Create the Circulation, which fails.

        restCirculationMockMvc.perform(post("/api/circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circulation)))
                .andExpect(status().isBadRequest());

        List<Circulation> circulations = circulationRepository.findAll();
        assertThat(circulations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCirculations() throws Exception {
        // Initialize the database
        circulationRepository.saveAndFlush(circulation);

        // Get all the circulations
        restCirculationMockMvc.perform(get("/api/circulations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(circulation.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCirculation() throws Exception {
        // Initialize the database
        circulationRepository.saveAndFlush(circulation);

        // Get the circulation
        restCirculationMockMvc.perform(get("/api/circulations/{id}", circulation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(circulation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCirculation() throws Exception {
        // Get the circulation
        restCirculationMockMvc.perform(get("/api/circulations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCirculation() throws Exception {
        // Initialize the database
        circulationService.save(circulation);

        int databaseSizeBeforeUpdate = circulationRepository.findAll().size();

        // Update the circulation
        Circulation updatedCirculation = circulationRepository.findOne(circulation.getId());
        updatedCirculation
                .name(UPDATED_NAME)
                .price(UPDATED_PRICE)
                .description(UPDATED_DESCRIPTION);

        restCirculationMockMvc.perform(put("/api/circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCirculation)))
                .andExpect(status().isOk());

        // Validate the Circulation in the database
        List<Circulation> circulations = circulationRepository.findAll();
        assertThat(circulations).hasSize(databaseSizeBeforeUpdate);
        Circulation testCirculation = circulations.get(circulations.size() - 1);
        assertThat(testCirculation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCirculation.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCirculation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Circulation in ElasticSearch
        Circulation circulationEs = circulationSearchRepository.findOne(testCirculation.getId());
        assertThat(circulationEs).isEqualToComparingFieldByField(testCirculation);
    }

    @Test
    @Transactional
    public void deleteCirculation() throws Exception {
        // Initialize the database
        circulationService.save(circulation);

        int databaseSizeBeforeDelete = circulationRepository.findAll().size();

        // Get the circulation
        restCirculationMockMvc.perform(delete("/api/circulations/{id}", circulation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean circulationExistsInEs = circulationSearchRepository.exists(circulation.getId());
        assertThat(circulationExistsInEs).isFalse();

        // Validate the database is empty
        List<Circulation> circulations = circulationRepository.findAll();
        assertThat(circulations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCirculation() throws Exception {
        // Initialize the database
        circulationService.save(circulation);

        // Search the circulation
        restCirculationMockMvc.perform(get("/api/_search/circulations?query=id:" + circulation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circulation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
