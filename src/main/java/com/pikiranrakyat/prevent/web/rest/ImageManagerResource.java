package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.service.ImageManagerService;
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
 * REST controller for managing ImageManager.
 */
@RestController
@RequestMapping("/api")
public class ImageManagerResource {

    private final Logger log = LoggerFactory.getLogger(ImageManagerResource.class);

    @Inject
    private ImageManagerService imageManagerService;


    /**
     * DELETE  /image-managers/:id : delete the "id" imageManager.
     *
     * @param id the id of the imageManager to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/image-managers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteImageManager(@PathVariable Long id) {
        log.debug("REST request to delete ImageManager : {}", id);
        imageManagerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imageManager", id.toString())).build();
    }

}
