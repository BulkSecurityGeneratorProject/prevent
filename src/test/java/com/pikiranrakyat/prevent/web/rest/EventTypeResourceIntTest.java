package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.EventType;
import com.pikiranrakyat.prevent.repository.EventTypeRepository;
import com.pikiranrakyat.prevent.service.EventTypeService;
import com.pikiranrakyat.prevent.repository.search.EventTypeSearchRepository;

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
 * Test class for the EventTypeResource REST controller.
 *
 * @see EventTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class EventTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private EventTypeRepository eventTypeRepository;

    @Inject
    private EventTypeService eventTypeService;

    @Inject
    private EventTypeSearchRepository eventTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEventTypeMockMvc;

    private EventType eventType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventTypeResource eventTypeResource = new EventTypeResource();
        ReflectionTestUtils.setField(eventTypeResource, "eventTypeService", eventTypeService);
        this.restEventTypeMockMvc = MockMvcBuilders.standaloneSetup(eventTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventType createEntity(EntityManager em) {
        EventType eventType = new EventType()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION);
        return eventType;
    }

    @Before
    public void initTest() {
        eventTypeSearchRepository.deleteAll();
        eventType = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventType() throws Exception {
        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();

        // Create the EventType

        restEventTypeMockMvc.perform(post("/api/event-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventType)))
                .andExpect(status().isCreated());

        // Validate the EventType in the database
        List<EventType> eventTypes = eventTypeRepository.findAll();
        assertThat(eventTypes).hasSize(databaseSizeBeforeCreate + 1);
        EventType testEventType = eventTypes.get(eventTypes.size() - 1);
        assertThat(testEventType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the EventType in ElasticSearch
        EventType eventTypeEs = eventTypeSearchRepository.findOne(testEventType.getId());
        assertThat(eventTypeEs).isEqualToComparingFieldByField(testEventType);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeRepository.findAll().size();
        // set the field null
        eventType.setName(null);

        // Create the EventType, which fails.

        restEventTypeMockMvc.perform(post("/api/event-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventType)))
                .andExpect(status().isBadRequest());

        List<EventType> eventTypes = eventTypeRepository.findAll();
        assertThat(eventTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventTypes() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypes
        restEventTypeMockMvc.perform(get("/api/event-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get the eventType
        restEventTypeMockMvc.perform(get("/api/event-types/{id}", eventType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventType() throws Exception {
        // Get the eventType
        restEventTypeMockMvc.perform(get("/api/event-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventType() throws Exception {
        // Initialize the database
        eventTypeService.save(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType
        EventType updatedEventType = eventTypeRepository.findOne(eventType.getId());
        updatedEventType
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION);

        restEventTypeMockMvc.perform(put("/api/event-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEventType)))
                .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypes = eventTypeRepository.findAll();
        assertThat(eventTypes).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypes.get(eventTypes.size() - 1);
        assertThat(testEventType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the EventType in ElasticSearch
        EventType eventTypeEs = eventTypeSearchRepository.findOne(testEventType.getId());
        assertThat(eventTypeEs).isEqualToComparingFieldByField(testEventType);
    }

    @Test
    @Transactional
    public void deleteEventType() throws Exception {
        // Initialize the database
        eventTypeService.save(eventType);

        int databaseSizeBeforeDelete = eventTypeRepository.findAll().size();

        // Get the eventType
        restEventTypeMockMvc.perform(delete("/api/event-types/{id}", eventType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean eventTypeExistsInEs = eventTypeSearchRepository.exists(eventType.getId());
        assertThat(eventTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<EventType> eventTypes = eventTypeRepository.findAll();
        assertThat(eventTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEventType() throws Exception {
        // Initialize the database
        eventTypeService.save(eventType);

        // Search the eventType
        restEventTypeMockMvc.perform(get("/api/_search/event-types?query=id:" + eventType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
