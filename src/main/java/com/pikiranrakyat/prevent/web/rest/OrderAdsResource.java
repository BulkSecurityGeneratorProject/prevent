package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.OrderAds;
import com.pikiranrakyat.prevent.service.OrderAdsService;
import com.pikiranrakyat.prevent.web.rest.util.HeaderUtil;
import com.pikiranrakyat.prevent.web.rest.util.PaginationUtil;
import com.pikiranrakyat.prevent.web.rest.vm.ManagedOrderAdsVM;
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
 * REST controller for managing OrderAds.
 */
@RestController
@RequestMapping("/api")
public class OrderAdsResource {

    private final Logger log = LoggerFactory.getLogger(OrderAdsResource.class);

    @Inject
    private OrderAdsService orderAdsService;

    /**
     * POST  /order-ads : Create a new orderAds.
     *
     * @param orderAds the orderAds to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderAds, or with status 400 (Bad Request) if the orderAds has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-ads",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderAds> createOrderAds(@Valid @RequestBody OrderAds orderAds) throws URISyntaxException {
        log.debug("REST request to save OrderAds : {}", orderAds);
        if (orderAds.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderAds", "idexists", "A new orderAds cannot already have an ID")).body(null);
        }
        orderAds.setOrderNumber(UUID.randomUUID().toString());
        orderAds.setTotal(orderAds.getAds().getTotalPrice());

        OrderAds result = orderAdsService.save(orderAds);
        return ResponseEntity.created(new URI("/api/order-ads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderAds", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-ads : Updates an existing orderAds.
     *
     * @param orderAds the orderAds to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderAds,
     * or with status 400 (Bad Request) if the orderAds is not valid,
     * or with status 500 (Internal Server Error) if the orderAds couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-ads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderAds> updateOrderAds(@Valid @RequestBody OrderAds orderAds) throws URISyntaxException {
        log.debug("REST request to update OrderAds : {}", orderAds);
        if (orderAds.getId() == null) {
            return createOrderAds(orderAds);
        }
        OrderAds result = orderAdsService.save(orderAds);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderAds", orderAds.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-ads : get all the orderAds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderAds in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-ads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedOrderAdsVM>> getAllOrderAds(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderAds");
        Page<OrderAds> page = orderAdsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-ads");
        return new ResponseEntity<>(page.getContent().stream().map(ManagedOrderAdsVM::new).collect(Collectors.toList()), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-ads/:id : get the "id" orderAds.
     *
     * @param id the id of the orderAds to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderAds, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-ads/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderAds> getOrderAds(@PathVariable Long id) {
        log.debug("REST request to get OrderAds : {}", id);
        OrderAds orderAds = orderAdsService.findOne(id);
        return Optional.ofNullable(orderAds)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-ads/:id : delete the "id" orderAds.
     *
     * @param id the id of the orderAds to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-ads/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderAds(@PathVariable Long id) {
        log.debug("REST request to delete OrderAds : {}", id);
        orderAdsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderAds", id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-ads?query=:query : search for the orderAds corresponding
     * to the query.
     *
     * @param query    the query of the orderAds search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/order-ads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<OrderAds>> searchOrderAds(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OrderAds for query {}", query);
        Page<OrderAds> page = orderAdsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-ads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
