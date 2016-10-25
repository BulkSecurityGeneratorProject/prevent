package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.repository.FileManagerRepository;
import com.pikiranrakyat.prevent.repository.search.FileManagerSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FileManager.
 */
@Service
@Transactional
public class FileManagerService {

    private final Logger log = LoggerFactory.getLogger(FileManagerService.class);

    @Inject
    private FileManagerRepository fileManagerRepository;

    @Inject
    private FileManagerSearchRepository fileManagerSearchRepository;

    /**
     * Save a fileManager.
     *
     * @param fileManager the entity to save
     * @return the persisted entity
     */
    public FileManager save(FileManager fileManager) {
        log.debug("Request to save FileManager : {}", fileManager);
        FileManager result = fileManagerRepository.save(fileManager);
        fileManagerSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the fileManagers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FileManager> findAll(Pageable pageable) {
        log.debug("Request to get all FileManagers");
        Page<FileManager> result = fileManagerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one fileManager by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public FileManager findOne(Long id) {
        log.debug("Request to get FileManager : {}", id);
        FileManager fileManager = fileManagerRepository.findOne(id);
        return fileManager;
    }

    /**
     *  Delete the  fileManager by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FileManager : {}", id);
        fileManagerRepository.delete(id);
        fileManagerSearchRepository.delete(id);
    }

    /**
     * Search for the fileManager corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FileManager> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FileManagers for query {}", query);
        Page<FileManager> result = fileManagerSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<FileManager> findOneByName(String name) {
        log.debug("Request to get FileManager : {}", name);
        Optional<FileManager> fileManager = fileManagerRepository.findOneByName(name);
        return fileManager;
    }

}
