package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.ImageManager;
import com.pikiranrakyat.prevent.repository.ImageManagerRepository;
import com.pikiranrakyat.prevent.repository.search.ImageManagerSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.File;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing FileManager.
 */
@Service
@Transactional
public class ImageManagerService {

    private final Logger log = LoggerFactory.getLogger(ImageManagerService.class);

    @Inject
    private ImageManagerRepository imageManagerRepository;

    @Inject
    private ImageManagerSearchRepository imageManagerSearchRepository;

    /**
     * Save a fileManager.
     *
     * @param fileManager the entity to save
     * @return the persisted entity
     */
    public ImageManager save(ImageManager fileManager) {
        log.debug("Request to save ImageManager : {}", fileManager);
        ImageManager result = imageManagerRepository.save(fileManager);
        imageManagerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the fileManagers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ImageManager> findAll(Pageable pageable) {
        log.debug("Request to get all ImageManagers");
        Page<ImageManager> result = imageManagerRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one fileManager by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ImageManager findOne(Long id) {
        log.debug("Request to get ImageManager : {}", id);
        ImageManager fileManager = imageManagerRepository.findOne(id);
        return fileManager;
    }

    /**
     * Delete the  fileManager by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ImageManager : {}", id);

        String path = imageManagerRepository.findOne(id).getPath();
        File file = new File(path);
        if (file.exists())
            file.delete();

        imageManagerRepository.delete(id);
        imageManagerSearchRepository.delete(id);
    }

    /**
     * Search for the fileManager corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ImageManager> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ImageManagers for query {}", query);
        Page<ImageManager> result = imageManagerSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<ImageManager> findOneByName(String name) {
        log.debug("Request to get ImageManager : {}", name);
        Optional<ImageManager> fileManager = imageManagerRepository.findOneByName(name);
        return fileManager;
    }

}
