package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.master.Ads;
import com.pikiranrakyat.prevent.domain.AdsCategory;
import com.pikiranrakyat.prevent.repository.AdsRepository;
import com.pikiranrakyat.prevent.service.AdsService;
import com.pikiranrakyat.prevent.repository.search.AdsSearchRepository;

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
 * Test class for the AdsResource REST controller.
 *
 * @see AdsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class AdsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_COLS = 0;
    private static final Integer UPDATED_COLS = 1;

    private static final Integer DEFAULT_MILLIMETER = 0;
    private static final Integer UPDATED_MILLIMETER = 1;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private AdsRepository adsRepository;

    @Inject
    private AdsService adsService;

    @Inject
    private AdsSearchRepository adsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAdsMockMvc;

    private Ads ads;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdsResource adsResource = new AdsResource();
        ReflectionTestUtils.setField(adsResource, "adsService", adsService);
        this.restAdsMockMvc = MockMvcBuilders.standaloneSetup(adsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ads createEntity(EntityManager em) {
        Ads ads = new Ads()
                .name(DEFAULT_NAME)
                .cols(DEFAULT_COLS)
                .millimeter(DEFAULT_MILLIMETER)
                .totalPrice(DEFAULT_TOTAL_PRICE)
                .description(DEFAULT_DESCRIPTION);
        // Add required entity
        AdsCategory adsCategory = AdsCategoryResourceIntTest.createEntity(em);
        em.persist(adsCategory);
        em.flush();
        ads.setAdsCategory(adsCategory);
        return ads;
    }

    @Before
    public void initTest() {
        adsSearchRepository.deleteAll();
        ads = createEntity(em);
    }

    @Test
    @Transactional
    public void createAds() throws Exception {
        int databaseSizeBeforeCreate = adsRepository.findAll().size();

        // Create the Ads

        restAdsMockMvc.perform(post("/api/ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ads)))
                .andExpect(status().isCreated());

        // Validate the Ads in the database
        List<Ads> ads = adsRepository.findAll();
        assertThat(ads).hasSize(databaseSizeBeforeCreate + 1);
        Ads testAds = ads.get(ads.size() - 1);
        assertThat(testAds.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAds.getCols()).isEqualTo(DEFAULT_COLS);
        assertThat(testAds.getMillimeter()).isEqualTo(DEFAULT_MILLIMETER);
        assertThat(testAds.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testAds.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Ads in ElasticSearch
        Ads adsEs = adsSearchRepository.findOne(testAds.getId());
        assertThat(adsEs).isEqualToComparingFieldByField(testAds);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adsRepository.findAll().size();
        // set the field null
        ads.setName(null);

        // Create the Ads, which fails.

        restAdsMockMvc.perform(post("/api/ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ads)))
                .andExpect(status().isBadRequest());

        List<Ads> ads = adsRepository.findAll();
        assertThat(ads).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColsIsRequired() throws Exception {
        int databaseSizeBeforeTest = adsRepository.findAll().size();
        // set the field null
        ads.setCols(null);

        // Create the Ads, which fails.

        restAdsMockMvc.perform(post("/api/ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ads)))
                .andExpect(status().isBadRequest());

        List<Ads> ads = adsRepository.findAll();
        assertThat(ads).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMillimeterIsRequired() throws Exception {
        int databaseSizeBeforeTest = adsRepository.findAll().size();
        // set the field null
        ads.setMillimeter(null);

        // Create the Ads, which fails.

        restAdsMockMvc.perform(post("/api/ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ads)))
                .andExpect(status().isBadRequest());

        List<Ads> ads = adsRepository.findAll();
        assertThat(ads).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = adsRepository.findAll().size();
        // set the field null
        ads.setTotalPrice(null);

        // Create the Ads, which fails.

        restAdsMockMvc.perform(post("/api/ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ads)))
                .andExpect(status().isBadRequest());

        List<Ads> ads = adsRepository.findAll();
        assertThat(ads).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAds() throws Exception {
        // Initialize the database
        adsRepository.saveAndFlush(ads);

        // Get all the ads
        restAdsMockMvc.perform(get("/api/ads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ads.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].cols").value(hasItem(DEFAULT_COLS)))
                .andExpect(jsonPath("$.[*].millimeter").value(hasItem(DEFAULT_MILLIMETER)))
                .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAds() throws Exception {
        // Initialize the database
        adsRepository.saveAndFlush(ads);

        // Get the ads
        restAdsMockMvc.perform(get("/api/ads/{id}", ads.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ads.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cols").value(DEFAULT_COLS))
            .andExpect(jsonPath("$.millimeter").value(DEFAULT_MILLIMETER))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAds() throws Exception {
        // Get the ads
        restAdsMockMvc.perform(get("/api/ads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAds() throws Exception {
        // Initialize the database
        adsService.save(ads);

        int databaseSizeBeforeUpdate = adsRepository.findAll().size();

        // Update the ads
        Ads updatedAds = adsRepository.findOne(ads.getId());
        updatedAds
                .name(UPDATED_NAME)
                .cols(UPDATED_COLS)
                .millimeter(UPDATED_MILLIMETER)
                .totalPrice(UPDATED_TOTAL_PRICE)
                .description(UPDATED_DESCRIPTION);

        restAdsMockMvc.perform(put("/api/ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAds)))
                .andExpect(status().isOk());

        // Validate the Ads in the database
        List<Ads> ads = adsRepository.findAll();
        assertThat(ads).hasSize(databaseSizeBeforeUpdate);
        Ads testAds = ads.get(ads.size() - 1);
        assertThat(testAds.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAds.getCols()).isEqualTo(UPDATED_COLS);
        assertThat(testAds.getMillimeter()).isEqualTo(UPDATED_MILLIMETER);
        assertThat(testAds.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testAds.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Ads in ElasticSearch
        Ads adsEs = adsSearchRepository.findOne(testAds.getId());
        assertThat(adsEs).isEqualToComparingFieldByField(testAds);
    }

    @Test
    @Transactional
    public void deleteAds() throws Exception {
        // Initialize the database
        adsService.save(ads);

        int databaseSizeBeforeDelete = adsRepository.findAll().size();

        // Get the ads
        restAdsMockMvc.perform(delete("/api/ads/{id}", ads.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean adsExistsInEs = adsSearchRepository.exists(ads.getId());
        assertThat(adsExistsInEs).isFalse();

        // Validate the database is empty
        List<Ads> ads = adsRepository.findAll();
        assertThat(ads).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAds() throws Exception {
        // Initialize the database
        adsService.save(ads);

        // Search the ads
        restAdsMockMvc.perform(get("/api/_search/ads?query=id:" + ads.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ads.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cols").value(hasItem(DEFAULT_COLS)))
            .andExpect(jsonPath("$.[*].millimeter").value(hasItem(DEFAULT_MILLIMETER)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
