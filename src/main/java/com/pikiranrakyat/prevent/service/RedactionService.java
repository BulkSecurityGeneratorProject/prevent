package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.Redaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Redaction.
 */
public interface RedactionService {

    /**
     * Save a redaction.
     *
     * @param redaction the entity to save
     * @return the persisted entity
     */
    Redaction save(Redaction redaction);

    /**
     * Get all the redactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Redaction> findAll(Pageable pageable);

    List<Redaction> findAll();

    /**
     * Get the "id" redaction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Redaction findOne(Long id);

    /**
     * Delete the "id" redaction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the redaction corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Redaction> search(String query, Pageable pageable);
}
