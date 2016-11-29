package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;
import com.pikiranrakyat.prevent.domain.EventType;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.domain.Locations;
import com.pikiranrakyat.prevent.repository.EventsRepository;
import com.pikiranrakyat.prevent.repository.search.EventsSearchRepository;
import com.pikiranrakyat.prevent.service.EventsService;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventsResource REST controller.
 *
 * @see EventsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class EventsResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAA";
    private static final String UPDATED_TITLE = "BBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_STARTS = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_STARTS = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_STARTS_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_STARTS);

    private static final ZonedDateTime DEFAULT_ENDS = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_ENDS = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_ENDS_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_ENDS);

    private static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_SUBTOTAL = new BigDecimal(1);

    private static final Boolean DEFAULT_ACCEPT = false;
    private static final Boolean UPDATED_ACCEPT = true;

    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    private static final Boolean DEFAULT_IS_ORDER = false;
    private static final Boolean UPDATED_IS_ORDER = true;

    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private EventsService eventsService;

    @Inject
    private EventsSearchRepository eventsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEventsMockMvc;

    private Events events;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventsResource eventsResource = new EventsResource();
        ReflectionTestUtils.setField(eventsResource, "eventsService", eventsService);
        this.restEventsMockMvc = MockMvcBuilders.standaloneSetup(eventsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Events createEntity(EntityManager em) {
        Events events = new Events()
                .title(DEFAULT_TITLE)
                .description(DEFAULT_DESCRIPTION)
                .starts(DEFAULT_STARTS)
                .ends(DEFAULT_ENDS)
                .subtotal(DEFAULT_SUBTOTAL)
                .accept(DEFAULT_ACCEPT)
                .note(DEFAULT_NOTE)
                .isOrder(DEFAULT_IS_ORDER);
        // Add required entity
        EventType eventType = EventTypeResourceIntTest.createEntity(em);
        em.persist(eventType);
        em.flush();
        events.setEventType(eventType);
        // Add required entity
        Locations locations = LocationsResourceIntTest.createEntity(em);
        em.persist(locations);
        em.flush();
        events.setLocations(locations);
        return events;
    }

    @Before
    public void initTest() {
        eventsSearchRepository.deleteAll();
        events = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvents() throws Exception {
        int databaseSizeBeforeCreate = eventsRepository.findAll().size();

        // Create the Events

        restEventsMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(events)))
                .andExpect(status().isCreated());

        // Validate the Events in the database
        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeCreate + 1);
        Events testEvents = events.get(events.size() - 1);
        assertThat(testEvents.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEvents.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvents.getStarts()).isEqualTo(DEFAULT_STARTS);
        assertThat(testEvents.getEnds()).isEqualTo(DEFAULT_ENDS);
        assertThat(testEvents.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testEvents.isAccept()).isEqualTo(DEFAULT_ACCEPT);
        assertThat(testEvents.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testEvents.isIsOrder()).isEqualTo(DEFAULT_IS_ORDER);

        // Validate the Events in ElasticSearch
        Events eventsEs = eventsSearchRepository.findOne(testEvents.getId());
        assertThat(eventsEs).isEqualToComparingFieldByField(testEvents);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setTitle(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(events)))
                .andExpect(status().isBadRequest());

        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setDescription(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(events)))
                .andExpect(status().isBadRequest());

        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartsIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setStarts(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(events)))
                .andExpect(status().isBadRequest());

        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndsIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setEnds(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(events)))
                .andExpect(status().isBadRequest());

        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubtotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setSubtotal(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(events)))
                .andExpect(status().isBadRequest());

        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setAccept(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(events)))
                .andExpect(status().isBadRequest());

        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        // set the field null
        events.setIsOrder(null);

        // Create the Events, which fails.

        restEventsMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(events)))
                .andExpect(status().isBadRequest());

        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the events
        restEventsMockMvc.perform(get("/api/events?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].starts").value(hasItem(DEFAULT_STARTS_STR)))
                .andExpect(jsonPath("$.[*].ends").value(hasItem(DEFAULT_ENDS_STR)))
                .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.intValue())))
                .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
                .andExpect(jsonPath("$.[*].isOrder").value(hasItem(DEFAULT_IS_ORDER.booleanValue())));
    }

    @Test
    @Transactional
    public void getEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get the events
        restEventsMockMvc.perform(get("/api/events/{id}", events.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(events.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.starts").value(DEFAULT_STARTS_STR))
            .andExpect(jsonPath("$.ends").value(DEFAULT_ENDS_STR))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.intValue()))
            .andExpect(jsonPath("$.accept").value(DEFAULT_ACCEPT.booleanValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.isOrder").value(DEFAULT_IS_ORDER.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEvents() throws Exception {
        // Get the events
        restEventsMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvents() throws Exception {
        // Initialize the database
        eventsService.save(events);

        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Update the events
        Events updatedEvents = eventsRepository.findOne(events.getId());
        updatedEvents
                .title(UPDATED_TITLE)
                .description(UPDATED_DESCRIPTION)
                .starts(UPDATED_STARTS)
                .ends(UPDATED_ENDS)
                .subtotal(UPDATED_SUBTOTAL)
                .accept(UPDATED_ACCEPT)
                .note(UPDATED_NOTE)
                .isOrder(UPDATED_IS_ORDER);

        restEventsMockMvc.perform(put("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEvents)))
                .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = events.get(events.size() - 1);
        assertThat(testEvents.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvents.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvents.getStarts()).isEqualTo(UPDATED_STARTS);
        assertThat(testEvents.getEnds()).isEqualTo(UPDATED_ENDS);
        assertThat(testEvents.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testEvents.isAccept()).isEqualTo(UPDATED_ACCEPT);
        assertThat(testEvents.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testEvents.isIsOrder()).isEqualTo(UPDATED_IS_ORDER);

        // Validate the Events in ElasticSearch
        Events eventsEs = eventsSearchRepository.findOne(testEvents.getId());
        assertThat(eventsEs).isEqualToComparingFieldByField(testEvents);
    }

    @Test
    @Transactional
    public void deleteEvents() throws Exception {
        // Initialize the database
        eventsService.save(events);

        int databaseSizeBeforeDelete = eventsRepository.findAll().size();

        // Get the events
        restEventsMockMvc.perform(delete("/api/events/{id}", events.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean eventsExistsInEs = eventsSearchRepository.exists(events.getId());
        assertThat(eventsExistsInEs).isFalse();

        // Validate the database is empty
        List<Events> events = eventsRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEvents() throws Exception {
        // Initialize the database
        eventsService.save(events);

        // Search the events
        restEventsMockMvc.perform(get("/api/_search/events?query=id:" + events.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].starts").value(hasItem(DEFAULT_STARTS_STR)))
            .andExpect(jsonPath("$.[*].ends").value(hasItem(DEFAULT_ENDS_STR)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.intValue())))
            .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].isOrder").value(hasItem(DEFAULT_IS_ORDER.booleanValue())));
    }
}
