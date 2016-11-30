package com.pikiranrakyat.prevent.web.rest.manage;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.OrderMerchandise;
import com.pikiranrakyat.prevent.service.OrderMerchandiseService;
import com.pikiranrakyat.prevent.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Optional;

/**
 * REST controller for managing OrderMerchandise.
 */
@RestController
@RequestMapping("/api/manage")
public class ManageOrderMerchandiseResource {

    private final Logger log = LoggerFactory.getLogger(ManageOrderMerchandiseResource.class);

    @Inject
    private OrderMerchandiseService orderMerchandiseService;

    /**
     * GET  /order-merchandise/{id}/agree : agree orderMerchandises.
     *
     * @return the ResponseEntity with status 200 (OK) and the order merchandise accepted
     */
    @RequestMapping(value = "/order-merchandise/{id}/agree",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderMerchandise> agreeOrderMerchandise(@PathVariable Long id) {
        log.debug("REST request to get OrderMerchandise : {}", id);
        OrderMerchandise orderMerchandise = orderMerchandiseService.findOne(id);
        return Optional.ofNullable(orderMerchandise)
            .map(result -> {
                result.setAccept(Boolean.TRUE);
                orderMerchandiseService.save(result);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("orderMerchandise", "Order agree"))
                    .body(result);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * GET  /order-merchandise/{id}/disagree : disagree orderMerchandises.
     *
     * @return the ResponseEntity with status 200 (OK) and the order merchandise disagree
     */
    @RequestMapping(value = "/order-merchandise/{id}/disagree",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderMerchandise> disagreeOrderMerchandise(@PathVariable Long id) {
        log.debug("REST request to get OrderMerchandise : {}", id);
        OrderMerchandise orderMerchandise = orderMerchandiseService.findOne(id);
        return Optional.ofNullable(orderMerchandise)
            .map(result -> {
                result.setAccept(Boolean.FALSE);
                orderMerchandiseService.save(result);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("orderMerchandise", "Order dissagree"))
                    .body(result);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
