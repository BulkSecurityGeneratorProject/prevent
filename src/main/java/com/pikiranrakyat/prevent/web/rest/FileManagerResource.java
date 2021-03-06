package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.service.FileManagerService;
import com.pikiranrakyat.prevent.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

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

}
