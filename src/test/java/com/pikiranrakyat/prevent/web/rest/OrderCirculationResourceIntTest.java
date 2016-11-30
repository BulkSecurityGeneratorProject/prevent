package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.OrderCirculation;
import com.pikiranrakyat.prevent.domain.Redaction;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.repository.OrderCirculationRepository;
import com.pikiranrakyat.prevent.service.OrderCirculationService;
import com.pikiranrakyat.prevent.repository.search.OrderCirculationSearchRepository;

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
 * Test class for the OrderCirculationResource REST controller.
 *
 * @see OrderCirculationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class OrderCirculationResourceIntTest {

    private static final String DEFAULT_ORDER_NUMBER = "AAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBB";

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACCEPT = false;
    private static final Boolean UPDATED_ACCEPT = true;

    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    @Inject
    private OrderCirculationRepository orderCirculationRepository;

    @Inject
    private OrderCirculationService orderCirculationService;

    @Inject
    private OrderCirculationSearchRepository orderCirculationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrderCirculationMockMvc;

    private OrderCirculation orderCirculation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderCirculationResource orderCirculationResource = new OrderCirculationResource();
        ReflectionTestUtils.setField(orderCirculationResource, "orderCirculationService", orderCirculationService);
        this.restOrderCirculationMockMvc = MockMvcBuilders.standaloneSetup(orderCirculationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderCirculation createEntity(EntityManager em) {
        OrderCirculation orderCirculation = new OrderCirculation()
                .orderNumber(DEFAULT_ORDER_NUMBER)
                .title(DEFAULT_TITLE)
                .content(DEFAULT_CONTENT)
                .accept(DEFAULT_ACCEPT)
                .note(DEFAULT_NOTE);
        // Add required entity
        Redaction redaction = RedactionResourceIntTest.createEntity(em);
        em.persist(redaction);
        em.flush();
        orderCirculation.setRedaction(redaction);
        // Add required entity
        Events events = EventsResourceIntTest.createEntity(em);
        em.persist(events);
        em.flush();
        orderCirculation.setEvents(events);
        return orderCirculation;
    }

    @Before
    public void initTest() {
        orderCirculationSearchRepository.deleteAll();
        orderCirculation = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderCirculation() throws Exception {
        int databaseSizeBeforeCreate = orderCirculationRepository.findAll().size();

        // Create the OrderCirculation

        restOrderCirculationMockMvc.perform(post("/api/order-circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderCirculation)))
                .andExpect(status().isCreated());

        // Validate the OrderCirculation in the database
        List<OrderCirculation> orderCirculations = orderCirculationRepository.findAll();
        assertThat(orderCirculations).hasSize(databaseSizeBeforeCreate + 1);
        OrderCirculation testOrderCirculation = orderCirculations.get(orderCirculations.size() - 1);
        assertThat(testOrderCirculation.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testOrderCirculation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrderCirculation.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testOrderCirculation.isAccept()).isEqualTo(DEFAULT_ACCEPT);
        assertThat(testOrderCirculation.getNote()).isEqualTo(DEFAULT_NOTE);

        // Validate the OrderCirculation in ElasticSearch
        OrderCirculation orderCirculationEs = orderCirculationSearchRepository.findOne(testOrderCirculation.getId());
        assertThat(orderCirculationEs).isEqualToComparingFieldByField(testOrderCirculation);
    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderCirculationRepository.findAll().size();
        // set the field null
        orderCirculation.setOrderNumber(null);

        // Create the OrderCirculation, which fails.

        restOrderCirculationMockMvc.perform(post("/api/order-circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderCirculation)))
                .andExpect(status().isBadRequest());

        List<OrderCirculation> orderCirculations = orderCirculationRepository.findAll();
        assertThat(orderCirculations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderCirculationRepository.findAll().size();
        // set the field null
        orderCirculation.setTitle(null);

        // Create the OrderCirculation, which fails.

        restOrderCirculationMockMvc.perform(post("/api/order-circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderCirculation)))
                .andExpect(status().isBadRequest());

        List<OrderCirculation> orderCirculations = orderCirculationRepository.findAll();
        assertThat(orderCirculations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderCirculationRepository.findAll().size();
        // set the field null
        orderCirculation.setContent(null);

        // Create the OrderCirculation, which fails.

        restOrderCirculationMockMvc.perform(post("/api/order-circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderCirculation)))
                .andExpect(status().isBadRequest());

        List<OrderCirculation> orderCirculations = orderCirculationRepository.findAll();
        assertThat(orderCirculations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderCirculationRepository.findAll().size();
        // set the field null
        orderCirculation.setAccept(null);

        // Create the OrderCirculation, which fails.

        restOrderCirculationMockMvc.perform(post("/api/order-circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderCirculation)))
                .andExpect(status().isBadRequest());

        List<OrderCirculation> orderCirculations = orderCirculationRepository.findAll();
        assertThat(orderCirculations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderCirculations() throws Exception {
        // Initialize the database
        orderCirculationRepository.saveAndFlush(orderCirculation);

        // Get all the orderCirculations
        restOrderCirculationMockMvc.perform(get("/api/order-circulations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderCirculation.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    public void getOrderCirculation() throws Exception {
        // Initialize the database
        orderCirculationRepository.saveAndFlush(orderCirculation);

        // Get the orderCirculation
        restOrderCirculationMockMvc.perform(get("/api/order-circulations/{id}", orderCirculation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderCirculation.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.accept").value(DEFAULT_ACCEPT.booleanValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderCirculation() throws Exception {
        // Get the orderCirculation
        restOrderCirculationMockMvc.perform(get("/api/order-circulations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderCirculation() throws Exception {
        // Initialize the database
        orderCirculationService.save(orderCirculation);

        int databaseSizeBeforeUpdate = orderCirculationRepository.findAll().size();

        // Update the orderCirculation
        OrderCirculation updatedOrderCirculation = orderCirculationRepository.findOne(orderCirculation.getId());
        updatedOrderCirculation
                .orderNumber(UPDATED_ORDER_NUMBER)
                .title(UPDATED_TITLE)
                .content(UPDATED_CONTENT)
                .accept(UPDATED_ACCEPT)
                .note(UPDATED_NOTE);

        restOrderCirculationMockMvc.perform(put("/api/order-circulations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderCirculation)))
                .andExpect(status().isOk());

        // Validate the OrderCirculation in the database
        List<OrderCirculation> orderCirculations = orderCirculationRepository.findAll();
        assertThat(orderCirculations).hasSize(databaseSizeBeforeUpdate);
        OrderCirculation testOrderCirculation = orderCirculations.get(orderCirculations.size() - 1);
        assertThat(testOrderCirculation.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testOrderCirculation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOrderCirculation.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testOrderCirculation.isAccept()).isEqualTo(UPDATED_ACCEPT);
        assertThat(testOrderCirculation.getNote()).isEqualTo(UPDATED_NOTE);

        // Validate the OrderCirculation in ElasticSearch
        OrderCirculation orderCirculationEs = orderCirculationSearchRepository.findOne(testOrderCirculation.getId());
        assertThat(orderCirculationEs).isEqualToComparingFieldByField(testOrderCirculation);
    }

    @Test
    @Transactional
    public void deleteOrderCirculation() throws Exception {
        // Initialize the database
        orderCirculationService.save(orderCirculation);

        int databaseSizeBeforeDelete = orderCirculationRepository.findAll().size();

        // Get the orderCirculation
        restOrderCirculationMockMvc.perform(delete("/api/order-circulations/{id}", orderCirculation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderCirculationExistsInEs = orderCirculationSearchRepository.exists(orderCirculation.getId());
        assertThat(orderCirculationExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderCirculation> orderCirculations = orderCirculationRepository.findAll();
        assertThat(orderCirculations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderCirculation() throws Exception {
        // Initialize the database
        orderCirculationService.save(orderCirculation);

        // Search the orderCirculation
        restOrderCirculationMockMvc.perform(get("/api/_search/order-circulations?query=id:" + orderCirculation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderCirculation.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }
}
