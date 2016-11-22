package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.ContactPerson;
import com.pikiranrakyat.prevent.security.SecurityUtils;
import com.pikiranrakyat.prevent.service.ContactPersonService;
import com.pikiranrakyat.prevent.web.rest.util.HeaderUtil;
import com.pikiranrakyat.prevent.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
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
 * REST controller for managing ContactPerson.
 */
@RestController
@RequestMapping("/api")
public class ContactPersonResource {

    private final Logger log = LoggerFactory.getLogger(ContactPersonResource.class);

    @Inject
    private ContactPersonService contactPersonService;

    /**
     * POST  /contact-people : Create a new contactPerson.
     *
     * @param contactPerson the contactPerson to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactPerson, or with status 400 (Bad Request) if the contactPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contact-people",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactPerson> createContactPerson(@Valid @RequestBody ContactPerson contactPerson) throws URISyntaxException {
        log.debug("REST request to save ContactPerson : {}", contactPerson);
        if (contactPerson.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contactPerson", "idexists", "A new contactPerson cannot already have an ID")).body(null);
        }
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        try {
            ContactPerson result = contactPersonService.save(contactPerson, currentUserLogin);
            return ResponseEntity.created(new URI("/api/contact-people/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("contactPerson", result.getId().toString()))
                .body(result);
        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contactPerson", "Email exists", "Email sudah ada")).body(null);
            }

            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contactPerson", "", e.getMessage())).body(null);

        }

    }

    /**
     * PUT  /contact-people : Updates an existing contactPerson.
     *
     * @param contactPerson the contactPerson to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactPerson,
     * or with status 400 (Bad Request) if the contactPerson is not valid,
     * or with status 500 (Internal Server Error) if the contactPerson couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contact-people",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactPerson> updateContactPerson(@Valid @RequestBody ContactPerson contactPerson) throws URISyntaxException {
        log.debug("REST request to update ContactPerson : {}", contactPerson);
        if (contactPerson.getId() == null) {
            return createContactPerson(contactPerson);
        }
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        ContactPerson result = contactPersonService.save(contactPerson, currentUserLogin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contactPerson", contactPerson.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contact-people : get all the contactPeople.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactPeople in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/contact-people",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContactPerson>> getAllContactPeople(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ContactPeople");
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Page<ContactPerson> page = contactPersonService.findAllByUser(currentUserLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-people");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contact-people/:id : get the "id" contactPerson.
     *
     * @param id the id of the contactPerson to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactPerson, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contact-people/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactPerson> getContactPerson(@PathVariable Long id) {
        log.debug("REST request to get ContactPerson : {}", id);
        ContactPerson contactPerson = contactPersonService.findOne(id);
        return Optional.ofNullable(contactPerson)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contact-people/:id : delete the "id" contactPerson.
     *
     * @param id the id of the contactPerson to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contact-people/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContactPerson(@PathVariable Long id) {
        log.debug("REST request to delete ContactPerson : {}", id);
        contactPersonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contactPerson", id.toString())).build();
    }

    /**
     * SEARCH  /_search/contact-people?query=:query : search for the contactPerson corresponding
     * to the query.
     *
     * @param query    the query of the contactPerson search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/contact-people",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContactPerson>> searchContactPeople(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ContactPeople for query {}", query);
        Page<ContactPerson> page = contactPersonService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contact-people");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
