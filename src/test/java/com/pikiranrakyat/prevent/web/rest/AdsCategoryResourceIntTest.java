package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.AdsCategory;
import com.pikiranrakyat.prevent.repository.AdsCategoryRepository;
import com.pikiranrakyat.prevent.service.AdsCategoryService;
import com.pikiranrakyat.prevent.repository.search.AdsCategorySearchRepository;

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
 * Test class for the AdsCategoryResource REST controller.
 *
 * @see AdsCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class AdsCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private AdsCategoryRepository adsCategoryRepository;

    @Inject
    private AdsCategoryService adsCategoryService;

    @Inject
    private AdsCategorySearchRepository adsCategorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAdsCategoryMockMvc;

    private AdsCategory adsCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdsCategoryResource adsCategoryResource = new AdsCategoryResource();
        ReflectionTestUtils.setField(adsCategoryResource, "adsCategoryService", adsCategoryService);
        this.restAdsCategoryMockMvc = MockMvcBuilders.standaloneSetup(adsCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdsCategory createEntity(EntityManager em) {
        AdsCategory adsCategory = new AdsCategory()
                .name(DEFAULT_NAME)
                .price(DEFAULT_PRICE)
                .description(DEFAULT_DESCRIPTION);
        return adsCategory;
    }

    @Before
    public void initTest() {
        adsCategorySearchRepository.deleteAll();
        adsCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdsCategory() throws Exception {
        int databaseSizeBeforeCreate = adsCategoryRepository.findAll().size();

        // Create the AdsCategory

        restAdsCategoryMockMvc.perform(post("/api/ads-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adsCategory)))
                .andExpect(status().isCreated());

        // Validate the AdsCategory in the database
        List<AdsCategory> adsCategories = adsCategoryRepository.findAll();
        assertThat(adsCategories).hasSize(databaseSizeBeforeCreate + 1);
        AdsCategory testAdsCategory = adsCategories.get(adsCategories.size() - 1);
        assertThat(testAdsCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAdsCategory.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testAdsCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the AdsCategory in ElasticSearch
        AdsCategory adsCategoryEs = adsCategorySearchRepository.findOne(testAdsCategory.getId());
        assertThat(adsCategoryEs).isEqualToComparingFieldByField(testAdsCategory);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adsCategoryRepository.findAll().size();
        // set the field null
        adsCategory.setName(null);

        // Create the AdsCategory, which fails.

        restAdsCategoryMockMvc.perform(post("/api/ads-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adsCategory)))
                .andExpect(status().isBadRequest());

        List<AdsCategory> adsCategories = adsCategoryRepository.findAll();
        assertThat(adsCategories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = adsCategoryRepository.findAll().size();
        // set the field null
        adsCategory.setPrice(null);

        // Create the AdsCategory, which fails.

        restAdsCategoryMockMvc.perform(post("/api/ads-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adsCategory)))
                .andExpect(status().isBadRequest());

        List<AdsCategory> adsCategories = adsCategoryRepository.findAll();
        assertThat(adsCategories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdsCategories() throws Exception {
        // Initialize the database
        adsCategoryRepository.saveAndFlush(adsCategory);

        // Get all the adsCategories
        restAdsCategoryMockMvc.perform(get("/api/ads-categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(adsCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAdsCategory() throws Exception {
        // Initialize the database
        adsCategoryRepository.saveAndFlush(adsCategory);

        // Get the adsCategory
        restAdsCategoryMockMvc.perform(get("/api/ads-categories/{id}", adsCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adsCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdsCategory() throws Exception {
        // Get the adsCategory
        restAdsCategoryMockMvc.perform(get("/api/ads-categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdsCategory() throws Exception {
        // Initialize the database
        adsCategoryService.save(adsCategory);

        int databaseSizeBeforeUpdate = adsCategoryRepository.findAll().size();

        // Update the adsCategory
        AdsCategory updatedAdsCategory = adsCategoryRepository.findOne(adsCategory.getId());
        updatedAdsCategory
                .name(UPDATED_NAME)
                .price(UPDATED_PRICE)
                .description(UPDATED_DESCRIPTION);

        restAdsCategoryMockMvc.perform(put("/api/ads-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAdsCategory)))
                .andExpect(status().isOk());

        // Validate the AdsCategory in the database
        List<AdsCategory> adsCategories = adsCategoryRepository.findAll();
        assertThat(adsCategories).hasSize(databaseSizeBeforeUpdate);
        AdsCategory testAdsCategory = adsCategories.get(adsCategories.size() - 1);
        assertThat(testAdsCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdsCategory.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAdsCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the AdsCategory in ElasticSearch
        AdsCategory adsCategoryEs = adsCategorySearchRepository.findOne(testAdsCategory.getId());
        assertThat(adsCategoryEs).isEqualToComparingFieldByField(testAdsCategory);
    }

    @Test
    @Transactional
    public void deleteAdsCategory() throws Exception {
        // Initialize the database
        adsCategoryService.save(adsCategory);

        int databaseSizeBeforeDelete = adsCategoryRepository.findAll().size();

        // Get the adsCategory
        restAdsCategoryMockMvc.perform(delete("/api/ads-categories/{id}", adsCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean adsCategoryExistsInEs = adsCategorySearchRepository.exists(adsCategory.getId());
        assertThat(adsCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<AdsCategory> adsCategories = adsCategoryRepository.findAll();
        assertThat(adsCategories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAdsCategory() throws Exception {
        // Initialize the database
        adsCategoryService.save(adsCategory);

        // Search the adsCategory
        restAdsCategoryMockMvc.perform(get("/api/_search/ads-categories?query=id:" + adsCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adsCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
