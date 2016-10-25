package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.service.FileManagerService;
import com.pikiranrakyat.prevent.web.rest.util.HeaderUtil;
import com.pikiranrakyat.prevent.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing FileManager.
 */
@RestController
@RequestMapping("/api")
public class FileManagerResource {

    private final Logger log = LoggerFactory.getLogger(FileManagerResource.class);
        
    @Inject
    private FileManagerService fileManagerService;

    /**
     * POST  /file-managers : Create a new fileManager.
     *
     * @param fileManager the fileManager to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileManager, or with status 400 (Bad Request) if the fileManager has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/file-managers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileManager> createFileManager(@Valid @RequestBody FileManager fileManager) throws URISyntaxException {
        log.debug("REST request to save FileManager : {}", fileManager);
        if (fileManager.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fileManager", "idexists", "A new fileManager cannot already have an ID")).body(null);
        }
        FileManager result = fileManagerService.save(fileManager);
        return ResponseEntity.created(new URI("/api/file-managers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fileManager", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /file-managers : Updates an existing fileManager.
     *
     * @param fileManager the fileManager to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileManager,
     * or with status 400 (Bad Request) if the fileManager is not valid,
     * or with status 500 (Internal Server Error) if the fileManager couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/file-managers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileManager> updateFileManager(@Valid @RequestBody FileManager fileManager) throws URISyntaxException {
        log.debug("REST request to update FileManager : {}", fileManager);
        if (fileManager.getId() == null) {
            return createFileManager(fileManager);
        }
        FileManager result = fileManagerService.save(fileManager);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fileManager", fileManager.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-managers : get all the fileManagers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fileManagers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/file-managers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FileManager>> getAllFileManagers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FileManagers");
        Page<FileManager> page = fileManagerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/file-managers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /file-managers/:id : get the "id" fileManager.
     *
     * @param id the id of the fileManager to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileManager, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/file-managers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileManager> getFileManager(@PathVariable Long id) {
        log.debug("REST request to get FileManager : {}", id);
        FileManager fileManager = fileManagerService.findOne(id);
        return Optional.ofNullable(fileManager)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /file-managers/:id : delete the "id" fileManager.
     *
     * @param id the id of the fileManager to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/file-managers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFileManager(@PathVariable Long id) {
        log.debug("REST request to delete FileManager : {}", id);
        fileManagerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fileManager", id.toString())).build();
    }

    /**
     * SEARCH  /_search/file-managers?query=:query : search for the fileManager corresponding
     * to the query.
     *
     * @param query the query of the fileManager search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/file-managers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FileManager>> searchFileManagers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of FileManagers for query {}", query);
        Page<FileManager> page = fileManagerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/file-managers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
