package com.pikiranrakyat.prevent.web.rest.manage;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.Ads;
import com.pikiranrakyat.prevent.domain.Circulation;
import com.pikiranrakyat.prevent.domain.Merchandise;
import com.pikiranrakyat.prevent.domain.Redaction;
import com.pikiranrakyat.prevent.service.AdsService;
import com.pikiranrakyat.prevent.service.CirculationService;
import com.pikiranrakyat.prevent.service.MerchandiseService;
import com.pikiranrakyat.prevent.service.RedactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing Manage List Order.
 */
@RestController
@RequestMapping("/api")
public class ListOrderResource {

    private final Logger log = LoggerFactory.getLogger(ListOrderResource.class);

    @Inject
    private MerchandiseService merchandiseService;

    @Inject
    private CirculationService circulationService;

    @Inject
    private AdsService adsService;

    @Inject
    private RedactionService redactionService;

    /**
     * GET  /list/merchandise all get all order merchandise
     *
     * @return the ResponseEntity with status 200 (OK) and the order merchandise all
     */
    @RequestMapping(value = "/list/merchandise",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Merchandise> getAllMerchandise() {
        return merchandiseService.findAll();
    }


    /**
     * GET  /list/circulation all get all circulation
     *
     * @return the ResponseEntity with status 200 (OK) and the circulation all
     */
    @RequestMapping(value = "/list/circulation",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Circulation> getAllCirculation() {
        return circulationService.findAll();
    }


    /**
     * GET  /list/ads all get all ads
     *
     * @return the ResponseEntity with status 200 (OK) and the ads all
     */
    @RequestMapping(value = "/list/ads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ads> getAllAds() {
        return adsService.findAll();
    }


    /**
     * GET  /list/redaction all get all redaction
     *
     * @return the ResponseEntity with status 200 (OK) and the redaction all
     */
    @RequestMapping(value = "/list/redaction",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Redaction> getAllRedactions() {
        return redactionService.findAll();
    }
}
