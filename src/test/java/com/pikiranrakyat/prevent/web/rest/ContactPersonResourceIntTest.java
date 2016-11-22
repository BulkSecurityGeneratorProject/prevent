package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;
import com.pikiranrakyat.prevent.domain.ContactPerson;
import com.pikiranrakyat.prevent.repository.ContactPersonRepository;
import com.pikiranrakyat.prevent.repository.search.ContactPersonSearchRepository;
import com.pikiranrakyat.prevent.service.ContactPersonService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContactPersonResource REST controller.
 *
 * @see ContactPersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class ContactPersonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";

    @Inject
    private ContactPersonRepository contactPersonRepository;

    @Inject
    private ContactPersonService contactPersonService;

    @Inject
    private ContactPersonSearchRepository contactPersonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContactPersonMockMvc;

    private ContactPerson contactPerson;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactPersonResource contactPersonResource = new ContactPersonResource();
        ReflectionTestUtils.setField(contactPersonResource, "contactPersonService", contactPersonService);
        this.restContactPersonMockMvc = MockMvcBuilders.standaloneSetup(contactPersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactPerson createEntity(EntityManager em) {
        ContactPerson contactPerson = new ContactPerson()
                .name(DEFAULT_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE);
        return contactPerson;
    }


    @Before
    public void initTest() {
        contactPersonSearchRepository.deleteAll();
        contactPerson = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactPerson() throws Exception {
        int databaseSizeBeforeCreate = contactPersonRepository.findAll().size();

        // Create the ContactPerson

        restContactPersonMockMvc.perform(post("/api/contact-people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactPerson)))
                .andExpect(status().isCreated());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
        assertThat(contactPeople).hasSize(databaseSizeBeforeCreate + 1);
        ContactPerson testContactPerson = contactPeople.get(contactPeople.size() - 1);
        assertThat(testContactPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactPerson.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactPerson.getPhone()).isEqualTo(DEFAULT_PHONE);

        // Validate the ContactPerson in ElasticSearch
        ContactPerson contactPersonEs = contactPersonSearchRepository.findOne(testContactPerson.getId());
        assertThat(contactPersonEs).isEqualToComparingFieldByField(testContactPerson);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactPersonRepository.findAll().size();
        // set the field null
        contactPerson.setName(null);

        // Create the ContactPerson, which fails.

        restContactPersonMockMvc.perform(post("/api/contact-people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactPerson)))
                .andExpect(status().isBadRequest());

        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
        assertThat(contactPeople).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactPersonRepository.findAll().size();
        // set the field null
        contactPerson.setEmail(null);

        // Create the ContactPerson, which fails.

        restContactPersonMockMvc.perform(post("/api/contact-people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactPerson)))
                .andExpect(status().isBadRequest());

        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
        assertThat(contactPeople).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactPeople() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get all the contactPeople
        restContactPersonMockMvc.perform(get("/api/contact-people?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contactPerson.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getContactPerson() throws Exception {
        // Initialize the database
        contactPersonRepository.saveAndFlush(contactPerson);

        // Get the contactPerson
        restContactPersonMockMvc.perform(get("/api/contact-people/{id}", contactPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactPerson.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactPerson() throws Exception {
        // Get the contactPerson
        restContactPersonMockMvc.perform(get("/api/contact-people/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

//    @Test
//    @Transactional
//    public void updateContactPerson() throws Exception {
//        // Initialize the database
//        contactPersonService.save(contactPerson);
//
//        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
//
//        // Update the contactPerson
//        ContactPerson updatedContactPerson = contactPersonRepository.findOne(contactPerson.getId());
//        updatedContactPerson
//                .name(UPDATED_NAME)
//                .email(UPDATED_EMAIL)
//                .phone(UPDATED_PHONE);
//
//        restContactPersonMockMvc.perform(put("/api/contact-people")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(updatedContactPerson)))
//                .andExpect(status().isOk());
//
//        // Validate the ContactPerson in the database
//        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
//        assertThat(contactPeople).hasSize(databaseSizeBeforeUpdate);
//        ContactPerson testContactPerson = contactPeople.get(contactPeople.size() - 1);
//        assertThat(testContactPerson.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testContactPerson.getEmail()).isEqualTo(UPDATED_EMAIL);
//        assertThat(testContactPerson.getPhone()).isEqualTo(UPDATED_PHONE);
//
//        // Validate the ContactPerson in ElasticSearch
//        ContactPerson contactPersonEs = contactPersonSearchRepository.findOne(testContactPerson.getId());
//        assertThat(contactPersonEs).isEqualToComparingFieldByField(testContactPerson);
//    }

//    @Test
//    @Transactional
//    public void deleteContactPerson() throws Exception {
//        // Initialize the database
//        contactPersonService.save(contactPerson);
//
//        int databaseSizeBeforeDelete = contactPersonRepository.findAll().size();
//
//        // Get the contactPerson
//        restContactPersonMockMvc.perform(delete("/api/contact-people/{id}", contactPerson.getId())
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//        // Validate ElasticSearch is empty
//        boolean contactPersonExistsInEs = contactPersonSearchRepository.exists(contactPerson.getId());
//        assertThat(contactPersonExistsInEs).isFalse();
//
//        // Validate the database is empty
//        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
//        assertThat(contactPeople).hasSize(databaseSizeBeforeDelete - 1);
//    }

//    @Test
//    @Transactional
//    public void searchContactPerson() throws Exception {
//        // Initialize the database
//        contactPersonService.save(contactPerson);
//
//        // Search the contactPerson
//        restContactPersonMockMvc.perform(get("/api/_search/contact-people?query=id:" + contactPerson.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(contactPerson.getId().intValue())))
//            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
//            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
//            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
//    }
}
