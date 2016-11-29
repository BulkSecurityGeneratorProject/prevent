package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.domain.OrderMerchandise;
import com.pikiranrakyat.prevent.domain.Merchandise;
import com.pikiranrakyat.prevent.repository.OrderMerchandiseRepository;
import com.pikiranrakyat.prevent.repository.search.OrderMerchandiseSearchRepository;
import com.pikiranrakyat.prevent.service.OrderMerchandiseService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrderMerchandiseResource REST controller.
 *
 * @see OrderMerchandiseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class OrderMerchandiseResourceIntTest {

    private static final String DEFAULT_ORDER_NUMBER = "AAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBB";

    private static final Boolean DEFAULT_ACCEPT = false;
    private static final Boolean UPDATED_ACCEPT = true;

    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    private static final Integer DEFAULT_QTY = 0;
    private static final Integer UPDATED_QTY = 1;

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(1);

    @Inject
    private OrderMerchandiseRepository orderMerchandiseRepository;

    @Inject
    private OrderMerchandiseService orderMerchandiseService;

    @Inject
    private OrderMerchandiseSearchRepository orderMerchandiseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrderMerchandiseMockMvc;

    private OrderMerchandise orderMerchandise;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderMerchandiseResource orderMerchandiseResource = new OrderMerchandiseResource();
        ReflectionTestUtils.setField(orderMerchandiseResource, "orderMerchandiseService", orderMerchandiseService);
        this.restOrderMerchandiseMockMvc = MockMvcBuilders.standaloneSetup(orderMerchandiseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderMerchandise createEntity(EntityManager em) {
        OrderMerchandise orderMerchandise = new OrderMerchandise()
                .orderNumber(DEFAULT_ORDER_NUMBER)
                .accept(DEFAULT_ACCEPT)
                .note(DEFAULT_NOTE)
                .qty(DEFAULT_QTY)
                .total(DEFAULT_TOTAL);
        // Add required entity
        Merchandise merchandise = MerchandiseResourceIntTest.createEntity(em);
        em.persist(merchandise);
        em.flush();
        orderMerchandise.setMerchandise(merchandise);
        // Add required entity
        Events events = EventsResourceIntTest.createEntity(em);
        em.persist(events);
        em.flush();
        orderMerchandise.setEvents(events);
        return orderMerchandise;
    }

    @Before
    public void initTest() {
        orderMerchandiseSearchRepository.deleteAll();
        orderMerchandise = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderMerchandise() throws Exception {
        int databaseSizeBeforeCreate = orderMerchandiseRepository.findAll().size();

        // Create the OrderMerchandise

        restOrderMerchandiseMockMvc.perform(post("/api/order-merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderMerchandise)))
                .andExpect(status().isCreated());

        // Validate the OrderMerchandise in the database
        List<OrderMerchandise> orderMerchandises = orderMerchandiseRepository.findAll();
        assertThat(orderMerchandises).hasSize(databaseSizeBeforeCreate + 1);
        OrderMerchandise testOrderMerchandise = orderMerchandises.get(orderMerchandises.size() - 1);
        assertThat(testOrderMerchandise.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testOrderMerchandise.isAccept()).isEqualTo(DEFAULT_ACCEPT);
        assertThat(testOrderMerchandise.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testOrderMerchandise.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testOrderMerchandise.getTotal()).isEqualTo(DEFAULT_TOTAL);

        // Validate the OrderMerchandise in ElasticSearch
        OrderMerchandise orderMerchandiseEs = orderMerchandiseSearchRepository.findOne(testOrderMerchandise.getId());
        assertThat(orderMerchandiseEs).isEqualToComparingFieldByField(testOrderMerchandise);
    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderMerchandiseRepository.findAll().size();
        // set the field null
        orderMerchandise.setOrderNumber(null);

        // Create the OrderMerchandise, which fails.

        restOrderMerchandiseMockMvc.perform(post("/api/order-merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderMerchandise)))
                .andExpect(status().isBadRequest());

        List<OrderMerchandise> orderMerchandises = orderMerchandiseRepository.findAll();
        assertThat(orderMerchandises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderMerchandiseRepository.findAll().size();
        // set the field null
        orderMerchandise.setAccept(null);

        // Create the OrderMerchandise, which fails.

        restOrderMerchandiseMockMvc.perform(post("/api/order-merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderMerchandise)))
                .andExpect(status().isBadRequest());

        List<OrderMerchandise> orderMerchandises = orderMerchandiseRepository.findAll();
        assertThat(orderMerchandises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderMerchandiseRepository.findAll().size();
        // set the field null
        orderMerchandise.setQty(null);

        // Create the OrderMerchandise, which fails.

        restOrderMerchandiseMockMvc.perform(post("/api/order-merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderMerchandise)))
                .andExpect(status().isBadRequest());

        List<OrderMerchandise> orderMerchandises = orderMerchandiseRepository.findAll();
        assertThat(orderMerchandises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderMerchandiseRepository.findAll().size();
        // set the field null
        orderMerchandise.setTotal(null);

        // Create the OrderMerchandise, which fails.

        restOrderMerchandiseMockMvc.perform(post("/api/order-merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderMerchandise)))
                .andExpect(status().isBadRequest());

        List<OrderMerchandise> orderMerchandises = orderMerchandiseRepository.findAll();
        assertThat(orderMerchandises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderMerchandises() throws Exception {
        // Initialize the database
        orderMerchandiseRepository.saveAndFlush(orderMerchandise);

        // Get all the orderMerchandises
        restOrderMerchandiseMockMvc.perform(get("/api/order-merchandises?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderMerchandise.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
                .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }

    @Test
    @Transactional
    public void getOrderMerchandise() throws Exception {
        // Initialize the database
        orderMerchandiseRepository.saveAndFlush(orderMerchandise);

        // Get the orderMerchandise
        restOrderMerchandiseMockMvc.perform(get("/api/order-merchandises/{id}", orderMerchandise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderMerchandise.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.accept").value(DEFAULT_ACCEPT.booleanValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderMerchandise() throws Exception {
        // Get the orderMerchandise
        restOrderMerchandiseMockMvc.perform(get("/api/order-merchandises/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderMerchandise() throws Exception {
        // Initialize the database
        orderMerchandiseService.save(orderMerchandise);

        int databaseSizeBeforeUpdate = orderMerchandiseRepository.findAll().size();

        // Update the orderMerchandise
        OrderMerchandise updatedOrderMerchandise = orderMerchandiseRepository.findOne(orderMerchandise.getId());
        updatedOrderMerchandise
                .orderNumber(UPDATED_ORDER_NUMBER)
                .accept(UPDATED_ACCEPT)
                .note(UPDATED_NOTE)
                .qty(UPDATED_QTY)
                .total(UPDATED_TOTAL);

        restOrderMerchandiseMockMvc.perform(put("/api/order-merchandises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderMerchandise)))
                .andExpect(status().isOk());

        // Validate the OrderMerchandise in the database
        List<OrderMerchandise> orderMerchandises = orderMerchandiseRepository.findAll();
        assertThat(orderMerchandises).hasSize(databaseSizeBeforeUpdate);
        OrderMerchandise testOrderMerchandise = orderMerchandises.get(orderMerchandises.size() - 1);
        assertThat(testOrderMerchandise.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testOrderMerchandise.isAccept()).isEqualTo(UPDATED_ACCEPT);
        assertThat(testOrderMerchandise.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testOrderMerchandise.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testOrderMerchandise.getTotal()).isEqualTo(UPDATED_TOTAL);

        // Validate the OrderMerchandise in ElasticSearch
        OrderMerchandise orderMerchandiseEs = orderMerchandiseSearchRepository.findOne(testOrderMerchandise.getId());
        assertThat(orderMerchandiseEs).isEqualToComparingFieldByField(testOrderMerchandise);
    }

    @Test
    @Transactional
    public void deleteOrderMerchandise() throws Exception {
        // Initialize the database
        orderMerchandiseService.save(orderMerchandise);

        int databaseSizeBeforeDelete = orderMerchandiseRepository.findAll().size();

        // Get the orderMerchandise
        restOrderMerchandiseMockMvc.perform(delete("/api/order-merchandises/{id}", orderMerchandise.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderMerchandiseExistsInEs = orderMerchandiseSearchRepository.exists(orderMerchandise.getId());
        assertThat(orderMerchandiseExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderMerchandise> orderMerchandises = orderMerchandiseRepository.findAll();
        assertThat(orderMerchandises).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderMerchandise() throws Exception {
        // Initialize the database
        orderMerchandiseService.save(orderMerchandise);

        // Search the orderMerchandise
        restOrderMerchandiseMockMvc.perform(get("/api/_search/order-merchandises?query=id:" + orderMerchandise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderMerchandise.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }
}
