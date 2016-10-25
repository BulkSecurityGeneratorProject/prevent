package com.pikiranrakyat.prevent.web.rest;

import com.pikiranrakyat.prevent.PreventApp;

import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.repository.FileManagerRepository;
import com.pikiranrakyat.prevent.service.FileManagerService;
import com.pikiranrakyat.prevent.repository.search.FileManagerSearchRepository;

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
 * Test class for the FileManagerResource REST controller.
 *
 * @see FileManagerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
public class FileManagerResourceIntTest {

    private static final String DEFAULT_ORIGINAL = "AAAAA";
    private static final String UPDATED_ORIGINAL = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_PATH = "AAAAA";
    private static final String UPDATED_PATH = "BBBBB";

    private static final String DEFAULT_EXTENSION = "AAAAA";
    private static final String UPDATED_EXTENSION = "BBBBB";

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    @Inject
    private FileManagerRepository fileManagerRepository;

    @Inject
    private FileManagerService fileManagerService;

    @Inject
    private FileManagerSearchRepository fileManagerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFileManagerMockMvc;

    private FileManager fileManager;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FileManagerResource fileManagerResource = new FileManagerResource();
        ReflectionTestUtils.setField(fileManagerResource, "fileManagerService", fileManagerService);
        this.restFileManagerMockMvc = MockMvcBuilders.standaloneSetup(fileManagerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileManager createEntity(EntityManager em) {
        FileManager fileManager = new FileManager()
                .original(DEFAULT_ORIGINAL)
                .name(DEFAULT_NAME)
                .path(DEFAULT_PATH)
                .extension(DEFAULT_EXTENSION)
                .size(DEFAULT_SIZE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        fileManager.setUser(user);
        return fileManager;
    }

    @Before
    public void initTest() {
        fileManagerSearchRepository.deleteAll();
        fileManager = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileManager() throws Exception {
        int databaseSizeBeforeCreate = fileManagerRepository.findAll().size();

        // Create the FileManager

        restFileManagerMockMvc.perform(post("/api/file-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileManager)))
                .andExpect(status().isCreated());

        // Validate the FileManager in the database
        List<FileManager> fileManagers = fileManagerRepository.findAll();
        assertThat(fileManagers).hasSize(databaseSizeBeforeCreate + 1);
        FileManager testFileManager = fileManagers.get(fileManagers.size() - 1);
        assertThat(testFileManager.getOriginal()).isEqualTo(DEFAULT_ORIGINAL);
        assertThat(testFileManager.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFileManager.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testFileManager.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testFileManager.getSize()).isEqualTo(DEFAULT_SIZE);

        // Validate the FileManager in ElasticSearch
        FileManager fileManagerEs = fileManagerSearchRepository.findOne(testFileManager.getId());
        assertThat(fileManagerEs).isEqualToComparingFieldByField(testFileManager);
    }

    @Test
    @Transactional
    public void checkOriginalIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileManagerRepository.findAll().size();
        // set the field null
        fileManager.setOriginal(null);

        // Create the FileManager, which fails.

        restFileManagerMockMvc.perform(post("/api/file-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileManager)))
                .andExpect(status().isBadRequest());

        List<FileManager> fileManagers = fileManagerRepository.findAll();
        assertThat(fileManagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileManagerRepository.findAll().size();
        // set the field null
        fileManager.setName(null);

        // Create the FileManager, which fails.

        restFileManagerMockMvc.perform(post("/api/file-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileManager)))
                .andExpect(status().isBadRequest());

        List<FileManager> fileManagers = fileManagerRepository.findAll();
        assertThat(fileManagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileManagerRepository.findAll().size();
        // set the field null
        fileManager.setPath(null);

        // Create the FileManager, which fails.

        restFileManagerMockMvc.perform(post("/api/file-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileManager)))
                .andExpect(status().isBadRequest());

        List<FileManager> fileManagers = fileManagerRepository.findAll();
        assertThat(fileManagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExtensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileManagerRepository.findAll().size();
        // set the field null
        fileManager.setExtension(null);

        // Create the FileManager, which fails.

        restFileManagerMockMvc.perform(post("/api/file-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileManager)))
                .andExpect(status().isBadRequest());

        List<FileManager> fileManagers = fileManagerRepository.findAll();
        assertThat(fileManagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileManagerRepository.findAll().size();
        // set the field null
        fileManager.setSize(null);

        // Create the FileManager, which fails.

        restFileManagerMockMvc.perform(post("/api/file-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileManager)))
                .andExpect(status().isBadRequest());

        List<FileManager> fileManagers = fileManagerRepository.findAll();
        assertThat(fileManagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFileManagers() throws Exception {
        // Initialize the database
        fileManagerRepository.saveAndFlush(fileManager);

        // Get all the fileManagers
        restFileManagerMockMvc.perform(get("/api/file-managers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fileManager.getId().intValue())))
                .andExpect(jsonPath("$.[*].original").value(hasItem(DEFAULT_ORIGINAL.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
                .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION.toString())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())));
    }

    @Test
    @Transactional
    public void getFileManager() throws Exception {
        // Initialize the database
        fileManagerRepository.saveAndFlush(fileManager);

        // Get the fileManager
        restFileManagerMockMvc.perform(get("/api/file-managers/{id}", fileManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fileManager.getId().intValue()))
            .andExpect(jsonPath("$.original").value(DEFAULT_ORIGINAL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.extension").value(DEFAULT_EXTENSION.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFileManager() throws Exception {
        // Get the fileManager
        restFileManagerMockMvc.perform(get("/api/file-managers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileManager() throws Exception {
        // Initialize the database
        fileManagerService.save(fileManager);

        int databaseSizeBeforeUpdate = fileManagerRepository.findAll().size();

        // Update the fileManager
        FileManager updatedFileManager = fileManagerRepository.findOne(fileManager.getId());
        updatedFileManager
                .original(UPDATED_ORIGINAL)
                .name(UPDATED_NAME)
                .path(UPDATED_PATH)
                .extension(UPDATED_EXTENSION)
                .size(UPDATED_SIZE);

        restFileManagerMockMvc.perform(put("/api/file-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFileManager)))
                .andExpect(status().isOk());

        // Validate the FileManager in the database
        List<FileManager> fileManagers = fileManagerRepository.findAll();
        assertThat(fileManagers).hasSize(databaseSizeBeforeUpdate);
        FileManager testFileManager = fileManagers.get(fileManagers.size() - 1);
        assertThat(testFileManager.getOriginal()).isEqualTo(UPDATED_ORIGINAL);
        assertThat(testFileManager.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFileManager.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testFileManager.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testFileManager.getSize()).isEqualTo(UPDATED_SIZE);

        // Validate the FileManager in ElasticSearch
        FileManager fileManagerEs = fileManagerSearchRepository.findOne(testFileManager.getId());
        assertThat(fileManagerEs).isEqualToComparingFieldByField(testFileManager);
    }

    @Test
    @Transactional
    public void deleteFileManager() throws Exception {
        // Initialize the database
        fileManagerService.save(fileManager);

        int databaseSizeBeforeDelete = fileManagerRepository.findAll().size();

        // Get the fileManager
        restFileManagerMockMvc.perform(delete("/api/file-managers/{id}", fileManager.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean fileManagerExistsInEs = fileManagerSearchRepository.exists(fileManager.getId());
        assertThat(fileManagerExistsInEs).isFalse();

        // Validate the database is empty
        List<FileManager> fileManagers = fileManagerRepository.findAll();
        assertThat(fileManagers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFileManager() throws Exception {
        // Initialize the database
        fileManagerService.save(fileManager);

        // Search the fileManager
        restFileManagerMockMvc.perform(get("/api/_search/file-managers?query=id:" + fileManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].original").value(hasItem(DEFAULT_ORIGINAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())));
    }
}
