package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.OrderMerchandise;
import com.pikiranrakyat.prevent.service.OrderMerchandiseService;
import com.pikiranrakyat.prevent.web.rest.util.HeaderUtil;
import com.pikiranrakyat.prevent.web.rest.util.PaginationUtil;
import com.pikiranrakyat.prevent.web.rest.vm.ManagedOrderMerchandiseVM;
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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for managing OrderMerchandise.
 */
@RestController
@RequestMapping("/api")
public class OrderMerchandiseResource {

    private final Logger log = LoggerFactory.getLogger(OrderMerchandiseResource.class);

    @Inject
    private OrderMerchandiseService orderMerchandiseService;

    /**
     * POST  /order-merchandises : Create a new orderMerchandise.
     *
     * @param orderMerchandise the orderMerchandise to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderMerchandise, or with status 400 (Bad Request) if the orderMerchandise has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-merchandises",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderMerchandise> createOrderMerchandise(@Valid @RequestBody OrderMerchandise orderMerchandise) throws URISyntaxException {
        log.debug("REST request to save OrderMerchandise : {}", orderMerchandise);
        if (orderMerchandise.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderMerchandise", "idexists", "A new orderMerchandise cannot already have an ID")).body(null);
        }
        orderMerchandise.setOrderNumber(UUID.randomUUID().toString());
        OrderMerchandise result = orderMerchandiseService.save(orderMerchandise);
        return ResponseEntity.created(new URI("/api/order-merchandises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderMerchandise", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-merchandises : Updates an existing orderMerchandise.
     *
     * @param orderMerchandise the orderMerchandise to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderMerchandise,
     * or with status 400 (Bad Request) if the orderMerchandise is not valid,
     * or with status 500 (Internal Server Error) if the orderMerchandise couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-merchandises",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderMerchandise> updateOrderMerchandise(@Valid @RequestBody OrderMerchandise orderMerchandise) throws URISyntaxException {
        log.debug("REST request to update OrderMerchandise : {}", orderMerchandise);
        if (orderMerchandise.getId() == null) {
            return createOrderMerchandise(orderMerchandise);
        }
        OrderMerchandise result = orderMerchandiseService.save(orderMerchandise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderMerchandise", orderMerchandise.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-merchandises : get all the orderMerchandises.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderMerchandises in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-merchandises",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedOrderMerchandiseVM>> getAllOrderMerchandises(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderMerchandises");
        Page<OrderMerchandise> page = orderMerchandiseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-merchandises");
        return new ResponseEntity<>(page.getContent().stream().map(ManagedOrderMerchandiseVM::new).collect(Collectors.toList()), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-merchandises/:id : get the "id" orderMerchandise.
     *
     * @param id the id of the orderMerchandise to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderMerchandise, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-merchandises/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderMerchandise> getOrderMerchandise(@PathVariable Long id) {
        log.debug("REST request to get OrderMerchandise : {}", id);
        OrderMerchandise orderMerchandise = orderMerchandiseService.findOne(id);
        return Optional.ofNullable(orderMerchandise)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-merchandises/:id : delete the "id" orderMerchandise.
     *
     * @param id the id of the orderMerchandise to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-merchandises/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderMerchandise(@PathVariable Long id) {
        log.debug("REST request to delete OrderMerchandise : {}", id);
        orderMerchandiseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderMerchandise", id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-merchandises?query=:query : search for the orderMerchandise corresponding
     * to the query.
     *
     * @param query    the query of the orderMerchandise search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/order-merchandises",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OrderMerchandise>> searchOrderMerchandises(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OrderMerchandises for query {}", query);
        Page<OrderMerchandise> page = orderMerchandiseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-merchandises");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
