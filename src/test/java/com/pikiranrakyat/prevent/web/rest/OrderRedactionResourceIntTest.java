package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.OrderRedaction;
import com.pikiranrakyat.prevent.domain.Redaction;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.repository.OrderRedactionRepository;
import com.pikiranrakyat.prevent.service.OrderRedactionService;
import com.pikiranrakyat.prevent.repository.search.OrderRedactionSearchRepository;

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
 * Test class for the OrderRedactionResource REST controller.
 *
 * @see OrderRedactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class OrderRedactionResourceIntTest {

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
    private OrderRedactionRepository orderRedactionRepository;

    @Inject
    private OrderRedactionService orderRedactionService;

    @Inject
    private OrderRedactionSearchRepository orderRedactionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrderRedactionMockMvc;

    private OrderRedaction orderRedaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderRedactionResource orderRedactionResource = new OrderRedactionResource();
        ReflectionTestUtils.setField(orderRedactionResource, "orderRedactionService", orderRedactionService);
        this.restOrderRedactionMockMvc = MockMvcBuilders.standaloneSetup(orderRedactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderRedaction createEntity(EntityManager em) {
        OrderRedaction orderRedaction = new OrderRedaction()
                .orderNumber(DEFAULT_ORDER_NUMBER)
                .title(DEFAULT_TITLE)
                .content(DEFAULT_CONTENT)
                .accept(DEFAULT_ACCEPT)
                .note(DEFAULT_NOTE);
        // Add required entity
        Redaction redaction = RedactionResourceIntTest.createEntity(em);
        em.persist(redaction);
        em.flush();
        orderRedaction.setRedaction(redaction);
        // Add required entity
        Events events = EventsResourceIntTest.createEntity(em);
        em.persist(events);
        em.flush();
        orderRedaction.setEvents(events);
        return orderRedaction;
    }

    @Before
    public void initTest() {
        orderRedactionSearchRepository.deleteAll();
        orderRedaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderRedaction() throws Exception {
        int databaseSizeBeforeCreate = orderRedactionRepository.findAll().size();

        // Create the OrderRedaction

        restOrderRedactionMockMvc.perform(post("/api/order-redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderRedaction)))
                .andExpect(status().isCreated());

        // Validate the OrderRedaction in the database
        List<OrderRedaction> orderRedactions = orderRedactionRepository.findAll();
        assertThat(orderRedactions).hasSize(databaseSizeBeforeCreate + 1);
        OrderRedaction testOrderRedaction = orderRedactions.get(orderRedactions.size() - 1);
        assertThat(testOrderRedaction.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testOrderRedaction.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrderRedaction.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testOrderRedaction.isAccept()).isEqualTo(DEFAULT_ACCEPT);
        assertThat(testOrderRedaction.getNote()).isEqualTo(DEFAULT_NOTE);

        // Validate the OrderRedaction in ElasticSearch
        OrderRedaction orderRedactionEs = orderRedactionSearchRepository.findOne(testOrderRedaction.getId());
        assertThat(orderRedactionEs).isEqualToComparingFieldByField(testOrderRedaction);
    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRedactionRepository.findAll().size();
        // set the field null
        orderRedaction.setOrderNumber(null);

        // Create the OrderRedaction, which fails.

        restOrderRedactionMockMvc.perform(post("/api/order-redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderRedaction)))
                .andExpect(status().isBadRequest());

        List<OrderRedaction> orderRedactions = orderRedactionRepository.findAll();
        assertThat(orderRedactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRedactionRepository.findAll().size();
        // set the field null
        orderRedaction.setTitle(null);

        // Create the OrderRedaction, which fails.

        restOrderRedactionMockMvc.perform(post("/api/order-redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderRedaction)))
                .andExpect(status().isBadRequest());

        List<OrderRedaction> orderRedactions = orderRedactionRepository.findAll();
        assertThat(orderRedactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRedactionRepository.findAll().size();
        // set the field null
        orderRedaction.setContent(null);

        // Create the OrderRedaction, which fails.

        restOrderRedactionMockMvc.perform(post("/api/order-redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderRedaction)))
                .andExpect(status().isBadRequest());

        List<OrderRedaction> orderRedactions = orderRedactionRepository.findAll();
        assertThat(orderRedactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRedactionRepository.findAll().size();
        // set the field null
        orderRedaction.setAccept(null);

        // Create the OrderRedaction, which fails.

        restOrderRedactionMockMvc.perform(post("/api/order-redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderRedaction)))
                .andExpect(status().isBadRequest());

        List<OrderRedaction> orderRedactions = orderRedactionRepository.findAll();
        assertThat(orderRedactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderRedactions() throws Exception {
        // Initialize the database
        orderRedactionRepository.saveAndFlush(orderRedaction);

        // Get all the orderRedactions
        restOrderRedactionMockMvc.perform(get("/api/order-redactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderRedaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    public void getOrderRedaction() throws Exception {
        // Initialize the database
        orderRedactionRepository.saveAndFlush(orderRedaction);

        // Get the orderRedaction
        restOrderRedactionMockMvc.perform(get("/api/order-redactions/{id}", orderRedaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderRedaction.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.accept").value(DEFAULT_ACCEPT.booleanValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderRedaction() throws Exception {
        // Get the orderRedaction
        restOrderRedactionMockMvc.perform(get("/api/order-redactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderRedaction() throws Exception {
        // Initialize the database
        orderRedactionService.save(orderRedaction);

        int databaseSizeBeforeUpdate = orderRedactionRepository.findAll().size();

        // Update the orderRedaction
        OrderRedaction updatedOrderRedaction = orderRedactionRepository.findOne(orderRedaction.getId());
        updatedOrderRedaction
                .orderNumber(UPDATED_ORDER_NUMBER)
                .title(UPDATED_TITLE)
                .content(UPDATED_CONTENT)
                .accept(UPDATED_ACCEPT)
                .note(UPDATED_NOTE);

        restOrderRedactionMockMvc.perform(put("/api/order-redactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderRedaction)))
                .andExpect(status().isOk());

        // Validate the OrderRedaction in the database
        List<OrderRedaction> orderRedactions = orderRedactionRepository.findAll();
        assertThat(orderRedactions).hasSize(databaseSizeBeforeUpdate);
        OrderRedaction testOrderRedaction = orderRedactions.get(orderRedactions.size() - 1);
        assertThat(testOrderRedaction.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testOrderRedaction.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOrderRedaction.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testOrderRedaction.isAccept()).isEqualTo(UPDATED_ACCEPT);
        assertThat(testOrderRedaction.getNote()).isEqualTo(UPDATED_NOTE);

        // Validate the OrderRedaction in ElasticSearch
        OrderRedaction orderRedactionEs = orderRedactionSearchRepository.findOne(testOrderRedaction.getId());
        assertThat(orderRedactionEs).isEqualToComparingFieldByField(testOrderRedaction);
    }

    @Test
    @Transactional
    public void deleteOrderRedaction() throws Exception {
        // Initialize the database
        orderRedactionService.save(orderRedaction);

        int databaseSizeBeforeDelete = orderRedactionRepository.findAll().size();

        // Get the orderRedaction
        restOrderRedactionMockMvc.perform(delete("/api/order-redactions/{id}", orderRedaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderRedactionExistsInEs = orderRedactionSearchRepository.exists(orderRedaction.getId());
        assertThat(orderRedactionExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderRedaction> orderRedactions = orderRedactionRepository.findAll();
        assertThat(orderRedactions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderRedaction() throws Exception {
        // Initialize the database
        orderRedactionService.save(orderRedaction);

        // Search the orderRedaction
        restOrderRedactionMockMvc.perform(get("/api/_search/order-redactions?query=id:" + orderRedaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderRedaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }
}
