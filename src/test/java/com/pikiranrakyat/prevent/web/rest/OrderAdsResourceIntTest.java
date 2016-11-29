package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.domain.OrderAds;
import com.pikiranrakyat.prevent.domain.Ads;
import com.pikiranrakyat.prevent.repository.OrderAdsRepository;
import com.pikiranrakyat.prevent.repository.search.OrderAdsSearchRepository;
import com.pikiranrakyat.prevent.service.OrderAdsService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrderAdsResource REST controller.
 *
 * @see OrderAdsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class OrderAdsResourceIntTest {

    private static final String DEFAULT_ORDER_NUMBER = "AAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBB";

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    private static final Boolean DEFAULT_ACCEPT = false;
    private static final Boolean UPDATED_ACCEPT = true;

    private static final LocalDate DEFAULT_PUBLISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(1);

    @Inject
    private OrderAdsRepository orderAdsRepository;

    @Inject
    private OrderAdsService orderAdsService;

    @Inject
    private OrderAdsSearchRepository orderAdsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrderAdsMockMvc;

    private OrderAds orderAds;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderAdsResource orderAdsResource = new OrderAdsResource();
        ReflectionTestUtils.setField(orderAdsResource, "orderAdsService", orderAdsService);
        this.restOrderAdsMockMvc = MockMvcBuilders.standaloneSetup(orderAdsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderAds createEntity(EntityManager em) {
        OrderAds orderAds = new OrderAds()
                .orderNumber(DEFAULT_ORDER_NUMBER)
                .title(DEFAULT_TITLE)
                .content(DEFAULT_CONTENT)
                .note(DEFAULT_NOTE)
                .accept(DEFAULT_ACCEPT)
                .publishDate(DEFAULT_PUBLISH_DATE)
                .total(DEFAULT_TOTAL);
        // Add required entity
        Ads ads = AdsResourceIntTest.createEntity(em);
        em.persist(ads);
        em.flush();
        orderAds.setAds(ads);
        // Add required entity
        Events events = EventsResourceIntTest.createEntity(em);
        em.persist(events);
        em.flush();
        orderAds.setEvents(events);
        return orderAds;
    }

    @Before
    public void initTest() {
        orderAdsSearchRepository.deleteAll();
        orderAds = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderAds() throws Exception {
        int databaseSizeBeforeCreate = orderAdsRepository.findAll().size();

        // Create the OrderAds

        restOrderAdsMockMvc.perform(post("/api/order-ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderAds)))
                .andExpect(status().isCreated());

        // Validate the OrderAds in the database
        List<OrderAds> orderAds = orderAdsRepository.findAll();
        assertThat(orderAds).hasSize(databaseSizeBeforeCreate + 1);
        OrderAds testOrderAds = orderAds.get(orderAds.size() - 1);
        assertThat(testOrderAds.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testOrderAds.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrderAds.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testOrderAds.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testOrderAds.isAccept()).isEqualTo(DEFAULT_ACCEPT);
        assertThat(testOrderAds.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testOrderAds.getTotal()).isEqualTo(DEFAULT_TOTAL);

        // Validate the OrderAds in ElasticSearch
        OrderAds orderAdsEs = orderAdsSearchRepository.findOne(testOrderAds.getId());
        assertThat(orderAdsEs).isEqualToComparingFieldByField(testOrderAds);
    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderAdsRepository.findAll().size();
        // set the field null
        orderAds.setOrderNumber(null);

        // Create the OrderAds, which fails.

        restOrderAdsMockMvc.perform(post("/api/order-ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderAds)))
                .andExpect(status().isBadRequest());

        List<OrderAds> orderAds = orderAdsRepository.findAll();
        assertThat(orderAds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderAdsRepository.findAll().size();
        // set the field null
        orderAds.setTitle(null);

        // Create the OrderAds, which fails.

        restOrderAdsMockMvc.perform(post("/api/order-ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderAds)))
                .andExpect(status().isBadRequest());

        List<OrderAds> orderAds = orderAdsRepository.findAll();
        assertThat(orderAds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderAdsRepository.findAll().size();
        // set the field null
        orderAds.setContent(null);

        // Create the OrderAds, which fails.

        restOrderAdsMockMvc.perform(post("/api/order-ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderAds)))
                .andExpect(status().isBadRequest());

        List<OrderAds> orderAds = orderAdsRepository.findAll();
        assertThat(orderAds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderAdsRepository.findAll().size();
        // set the field null
        orderAds.setAccept(null);

        // Create the OrderAds, which fails.

        restOrderAdsMockMvc.perform(post("/api/order-ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderAds)))
                .andExpect(status().isBadRequest());

        List<OrderAds> orderAds = orderAdsRepository.findAll();
        assertThat(orderAds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderAdsRepository.findAll().size();
        // set the field null
        orderAds.setTotal(null);

        // Create the OrderAds, which fails.

        restOrderAdsMockMvc.perform(post("/api/order-ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderAds)))
                .andExpect(status().isBadRequest());

        List<OrderAds> orderAds = orderAdsRepository.findAll();
        assertThat(orderAds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderAds() throws Exception {
        // Initialize the database
        orderAdsRepository.saveAndFlush(orderAds);

        // Get all the orderAds
        restOrderAdsMockMvc.perform(get("/api/order-ads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderAds.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
                .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
                .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
                .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }

    @Test
    @Transactional
    public void getOrderAds() throws Exception {
        // Initialize the database
        orderAdsRepository.saveAndFlush(orderAds);

        // Get the orderAds
        restOrderAdsMockMvc.perform(get("/api/order-ads/{id}", orderAds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderAds.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.accept").value(DEFAULT_ACCEPT.booleanValue()))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderAds() throws Exception {
        // Get the orderAds
        restOrderAdsMockMvc.perform(get("/api/order-ads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderAds() throws Exception {
        // Initialize the database
        orderAdsService.save(orderAds);

        int databaseSizeBeforeUpdate = orderAdsRepository.findAll().size();

        // Update the orderAds
        OrderAds updatedOrderAds = orderAdsRepository.findOne(orderAds.getId());
        updatedOrderAds
                .orderNumber(UPDATED_ORDER_NUMBER)
                .title(UPDATED_TITLE)
                .content(UPDATED_CONTENT)
                .note(UPDATED_NOTE)
                .accept(UPDATED_ACCEPT)
                .publishDate(UPDATED_PUBLISH_DATE)
                .total(UPDATED_TOTAL);

        restOrderAdsMockMvc.perform(put("/api/order-ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderAds)))
                .andExpect(status().isOk());

        // Validate the OrderAds in the database
        List<OrderAds> orderAds = orderAdsRepository.findAll();
        assertThat(orderAds).hasSize(databaseSizeBeforeUpdate);
        OrderAds testOrderAds = orderAds.get(orderAds.size() - 1);
        assertThat(testOrderAds.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testOrderAds.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOrderAds.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testOrderAds.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testOrderAds.isAccept()).isEqualTo(UPDATED_ACCEPT);
        assertThat(testOrderAds.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testOrderAds.getTotal()).isEqualTo(UPDATED_TOTAL);

        // Validate the OrderAds in ElasticSearch
        OrderAds orderAdsEs = orderAdsSearchRepository.findOne(testOrderAds.getId());
        assertThat(orderAdsEs).isEqualToComparingFieldByField(testOrderAds);
    }

    @Test
    @Transactional
    public void deleteOrderAds() throws Exception {
        // Initialize the database
        orderAdsService.save(orderAds);

        int databaseSizeBeforeDelete = orderAdsRepository.findAll().size();

        // Get the orderAds
        restOrderAdsMockMvc.perform(delete("/api/order-ads/{id}", orderAds.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderAdsExistsInEs = orderAdsSearchRepository.exists(orderAds.getId());
        assertThat(orderAdsExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderAds> orderAds = orderAdsRepository.findAll();
        assertThat(orderAds).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderAds() throws Exception {
        // Initialize the database
        orderAdsService.save(orderAds);

        // Search the orderAds
        restOrderAdsMockMvc.perform(get("/api/_search/order-ads?query=id:" + orderAds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderAds.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }
}
