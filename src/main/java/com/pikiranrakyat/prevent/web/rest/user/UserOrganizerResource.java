package com.pikiranrakyat.prevent.web.rest.user;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.Organizer;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.exception.ResourceNotFoundException;
import com.pikiranrakyat.prevent.repository.OrganizerRepository;
import com.pikiranrakyat.prevent.repository.UserRepository;
import com.pikiranrakyat.prevent.security.SecurityUtils;
import com.pikiranrakyat.prevent.service.OrganizerService;
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

/**
 * REST controller for managing Organizer.
 */
@RestController
@RequestMapping("/api/user")
public class UserOrganizerResource {

    private final Logger log = LoggerFactory.getLogger(UserOrganizerResource.class);

    @Inject
    private OrganizerService organizerService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private OrganizerRepository organizerRepository;

    /**
     * POST  /organizers : Create a new organizer.
     *
     * @param organizer the organizer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organizer, or with status 400 (Bad Request) if the organizer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/organizers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizer> createOrganizer(@RequestBody Organizer organizer) throws URISyntaxException {
        log.debug("REST request to save Organizer : {}", organizer);
        if (organizer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("organizer", "idexists", "A new organizer cannot already have an ID")).body(null);
        }

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .orElseThrow(() -> new ResourceNotFoundException("User tidak ada"));
        organizer.setUser(user);

        Organizer result = organizerService.save(organizer);
        return ResponseEntity.created(new URI("/api/organizers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("organizer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organizers : Updates an existing organizer.
     *
     * @param organizer the organizer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organizer,
     * or with status 400 (Bad Request) if the organizer is not valid,
     * or with status 500 (Internal Server Error) if the organizer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/organizers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizer> updateOrganizer(@Valid @RequestBody Organizer organizer) throws URISyntaxException {
        log.debug("REST request to update Organizer : {}", organizer);
        if (organizer.getId() == null) {
            return createOrganizer(organizer);
        }
        Organizer result = organizerService.save(organizer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("organizer", organizer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organizers : get all the organizers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organizers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/organizers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Organizer>> getAllOrganizers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Organizers");
        Page<Organizer> page = organizerRepository.findByUserIsCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user/organizers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /organizers/list : get all the organizers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/organizers/list",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Organizer>> getAllOrganizersList()
        throws URISyntaxException {
        log.debug("REST request to get a page of Organizers");
        List<Organizer> byUserIsCurrentUser = organizerRepository.findByUserIsCurrentUser();
        return new ResponseEntity<>(byUserIsCurrentUser, HttpStatus.OK);
    }

    /**
     * GET  /organizers/:id : get the "id" organizer.
     *
     * @param id the id of the organizer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organizer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/organizers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizer> getOrganizer(@PathVariable Long id) {
        log.debug("REST request to get Organizer : {}", id);
        Organizer organizer = organizerService.findOne(id);
        return Optional.ofNullable(organizer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /organizers/:id : delete the "id" organizer.
     *
     * @param id the id of the organizer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/organizers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrganizer(@PathVariable Long id) {
        log.debug("REST request to delete Organizer : {}", id);
        organizerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("organizer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/organizers?query=:query : search for the organizer corresponding
     * to the query.
     *
     * @param query    the query of the organizer search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/organizers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Organizer>> searchOrganizers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Organizers for query {}", query);
        Page<Organizer> page = organizerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/organizers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
