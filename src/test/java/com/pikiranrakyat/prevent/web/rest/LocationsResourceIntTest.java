package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.master.Locations;
import com.pikiranrakyat.prevent.repository.LocationsRepository;
import com.pikiranrakyat.prevent.service.LocationsService;
import com.pikiranrakyat.prevent.repository.search.LocationsSearchRepository;

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
 * Test class for the LocationsResource REST controller.
 *
 * @see LocationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class LocationsResourceIntTest {

    private static final String DEFAULT_NAME = "AAA";
    private static final String UPDATED_NAME = "BBB";

    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";

    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    @Inject
    private LocationsRepository locationsRepository;

    @Inject
    private LocationsService locationsService;

    @Inject
    private LocationsSearchRepository locationsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLocationsMockMvc;

    private Locations locations;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocationsResource locationsResource = new LocationsResource();
        ReflectionTestUtils.setField(locationsResource, "locationsService", locationsService);
        this.restLocationsMockMvc = MockMvcBuilders.standaloneSetup(locationsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locations createEntity(EntityManager em) {
        Locations locations = new Locations()
                .name(DEFAULT_NAME)
                .address(DEFAULT_ADDRESS)
                .city(DEFAULT_CITY)
                .state(DEFAULT_STATE)
                .postalCode(DEFAULT_POSTAL_CODE)
                .latitude(DEFAULT_LATITUDE)
                .longitude(DEFAULT_LONGITUDE);
        return locations;
    }

    @Before
    public void initTest() {
        locationsSearchRepository.deleteAll();
        locations = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocations() throws Exception {
        int databaseSizeBeforeCreate = locationsRepository.findAll().size();

        // Create the Locations

        restLocationsMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locations)))
                .andExpect(status().isCreated());

        // Validate the Locations in the database
        List<Locations> locations = locationsRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeCreate + 1);
        Locations testLocations = locations.get(locations.size() - 1);
        assertThat(testLocations.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLocations.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLocations.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testLocations.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testLocations.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testLocations.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testLocations.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);

        // Validate the Locations in ElasticSearch
        Locations locationsEs = locationsSearchRepository.findOne(testLocations.getId());
        assertThat(locationsEs).isEqualToComparingFieldByField(testLocations);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationsRepository.findAll().size();
        // set the field null
        locations.setName(null);

        // Create the Locations, which fails.

        restLocationsMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locations)))
                .andExpect(status().isBadRequest());

        List<Locations> locations = locationsRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationsRepository.findAll().size();
        // set the field null
        locations.setAddress(null);

        // Create the Locations, which fails.

        restLocationsMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locations)))
                .andExpect(status().isBadRequest());

        List<Locations> locations = locationsRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationsRepository.findAll().size();
        // set the field null
        locations.setCity(null);

        // Create the Locations, which fails.

        restLocationsMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locations)))
                .andExpect(status().isBadRequest());

        List<Locations> locations = locationsRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationsRepository.findAll().size();
        // set the field null
        locations.setState(null);

        // Create the Locations, which fails.

        restLocationsMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locations)))
                .andExpect(status().isBadRequest());

        List<Locations> locations = locationsRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationsRepository.findAll().size();
        // set the field null
        locations.setPostalCode(null);

        // Create the Locations, which fails.

        restLocationsMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locations)))
                .andExpect(status().isBadRequest());

        List<Locations> locations = locationsRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locations
        restLocationsMockMvc.perform(get("/api/locations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(locations.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @Test
    @Transactional
    public void getLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get the locations
        restLocationsMockMvc.perform(get("/api/locations/{id}", locations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(locations.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLocations() throws Exception {
        // Get the locations
        restLocationsMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocations() throws Exception {
        // Initialize the database
        locationsService.save(locations);

        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();

        // Update the locations
        Locations updatedLocations = locationsRepository.findOne(locations.getId());
        updatedLocations
                .name(UPDATED_NAME)
                .address(UPDATED_ADDRESS)
                .city(UPDATED_CITY)
                .state(UPDATED_STATE)
                .postalCode(UPDATED_POSTAL_CODE)
                .latitude(UPDATED_LATITUDE)
                .longitude(UPDATED_LONGITUDE);

        restLocationsMockMvc.perform(put("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLocations)))
                .andExpect(status().isOk());

        // Validate the Locations in the database
        List<Locations> locations = locationsRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeUpdate);
        Locations testLocations = locations.get(locations.size() - 1);
        assertThat(testLocations.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLocations.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLocations.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocations.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testLocations.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocations.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocations.getLongitude()).isEqualTo(UPDATED_LONGITUDE);

        // Validate the Locations in ElasticSearch
        Locations locationsEs = locationsSearchRepository.findOne(testLocations.getId());
        assertThat(locationsEs).isEqualToComparingFieldByField(testLocations);
    }

    @Test
    @Transactional
    public void deleteLocations() throws Exception {
        // Initialize the database
        locationsService.save(locations);

        int databaseSizeBeforeDelete = locationsRepository.findAll().size();

        // Get the locations
        restLocationsMockMvc.perform(delete("/api/locations/{id}", locations.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean locationsExistsInEs = locationsSearchRepository.exists(locations.getId());
        assertThat(locationsExistsInEs).isFalse();

        // Validate the database is empty
        List<Locations> locations = locationsRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLocations() throws Exception {
        // Initialize the database
        locationsService.save(locations);

        // Search the locations
        restLocationsMockMvc.perform(get("/api/_search/locations?query=id:" + locations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locations.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }
}
