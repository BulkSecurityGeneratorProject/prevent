package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.Merchandise;
import com.pikiranrakyat.prevent.repository.MerchandiseRepository;
import com.pikiranrakyat.prevent.service.MerchandiseService;
import com.pikiranrakyat.prevent.repository.search.MerchandiseSearchRepository;

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
 * Test class for the MerchandiseResource REST controller.
 *
 * @see MerchandiseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class MerchandiseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private MerchandiseRepository merchandiseRepository;

    @Inject
    private MerchandiseService merchandiseService;

    @Inject
    private MerchandiseSearchRepository merchandiseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMerchandiseMockMvc;

    private Merchandise merchandise;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MerchandiseResource merchandiseResource = new MerchandiseResource();
        ReflectionTestUtils.setField(merchandiseResource, "merchandiseService", merchandiseService);
        this.restMerchandiseMockMvc = MockMvcBuilders.standaloneSetup(merchandiseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchandise createEntity(EntityManager em) {
        Merchandise merchandise = new Merchandise()
                .name(DEFAULT_NAME)
                .price(DEFAULT_PRICE)
                .description(DEFAULT_DESCRIPTION);
        return merchandise;
    }

    @Before
    public void initTest() {
        merchandiseSearchRepository.deleteAll();
        merchandise = createEntity(em);
    }

    @Test
    @Transactional
    public void createMerchandise() throws Exception {
        int databaseSizeBeforeCreate = merchandiseRepository.findAll().size();

        // Create the Merchandise

        restMerchandiseMockMvc.perform(post("/api/merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchandise)))
                .andExpect(status().isCreated());

        // Validate the Merchandise in the database
        List<Merchandise> merchandises = merchandiseRepository.findAll();
        assertThat(merchandises).hasSize(databaseSizeBeforeCreate + 1);
        Merchandise testMerchandise = merchandises.get(merchandises.size() - 1);
        assertThat(testMerchandise.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMerchandise.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMerchandise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Merchandise in ElasticSearch
        Merchandise merchandiseEs = merchandiseSearchRepository.findOne(testMerchandise.getId());
        assertThat(merchandiseEs).isEqualToComparingFieldByField(testMerchandise);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchandiseRepository.findAll().size();
        // set the field null
        merchandise.setName(null);

        // Create the Merchandise, which fails.

        restMerchandiseMockMvc.perform(post("/api/merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchandise)))
                .andExpect(status().isBadRequest());

        List<Merchandise> merchandises = merchandiseRepository.findAll();
        assertThat(merchandises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchandiseRepository.findAll().size();
        // set the field null
        merchandise.setPrice(null);

        // Create the Merchandise, which fails.

        restMerchandiseMockMvc.perform(post("/api/merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(merchandise)))
                .andExpect(status().isBadRequest());

        List<Merchandise> merchandises = merchandiseRepository.findAll();
        assertThat(merchandises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMerchandises() throws Exception {
        // Initialize the database
        merchandiseRepository.saveAndFlush(merchandise);

        // Get all the merchandises
        restMerchandiseMockMvc.perform(get("/api/merchandises?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(merchandise.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMerchandise() throws Exception {
        // Initialize the database
        merchandiseRepository.saveAndFlush(merchandise);

        // Get the merchandise
        restMerchandiseMockMvc.perform(get("/api/merchandises/{id}", merchandise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(merchandise.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMerchandise() throws Exception {
        // Get the merchandise
        restMerchandiseMockMvc.perform(get("/api/merchandises/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMerchandise() throws Exception {
        // Initialize the database
        merchandiseService.save(merchandise);

        int databaseSizeBeforeUpdate = merchandiseRepository.findAll().size();

        // Update the merchandise
        Merchandise updatedMerchandise = merchandiseRepository.findOne(merchandise.getId());
        updatedMerchandise
                .name(UPDATED_NAME)
                .price(UPDATED_PRICE)
                .description(UPDATED_DESCRIPTION);

        restMerchandiseMockMvc.perform(put("/api/merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMerchandise)))
                .andExpect(status().isOk());

        // Validate the Merchandise in the database
        List<Merchandise> merchandises = merchandiseRepository.findAll();
        assertThat(merchandises).hasSize(databaseSizeBeforeUpdate);
        Merchandise testMerchandise = merchandises.get(merchandises.size() - 1);
        assertThat(testMerchandise.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMerchandise.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMerchandise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Merchandise in ElasticSearch
        Merchandise merchandiseEs = merchandiseSearchRepository.findOne(testMerchandise.getId());
        assertThat(merchandiseEs).isEqualToComparingFieldByField(testMerchandise);
    }

    @Test
    @Transactional
    public void deleteMerchandise() throws Exception {
        // Initialize the database
        merchandiseService.save(merchandise);

        int databaseSizeBeforeDelete = merchandiseRepository.findAll().size();

        // Get the merchandise
        restMerchandiseMockMvc.perform(delete("/api/merchandises/{id}", merchandise.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean merchandiseExistsInEs = merchandiseSearchRepository.exists(merchandise.getId());
        assertThat(merchandiseExistsInEs).isFalse();

        // Validate the database is empty
        List<Merchandise> merchandises = merchandiseRepository.findAll();
        assertThat(merchandises).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMerchandise() throws Exception {
        // Initialize the database
        merchandiseService.save(merchandise);

        // Search the merchandise
        restMerchandiseMockMvc.perform(get("/api/_search/merchandises?query=id:" + merchandise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchandise.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
