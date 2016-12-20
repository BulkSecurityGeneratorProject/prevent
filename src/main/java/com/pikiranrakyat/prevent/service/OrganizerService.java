package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.Organizer;
import com.pikiranrakyat.prevent.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Organizer.
 */
public interface OrganizerService {

    /**
     * Save a organizer.
     *
     * @param organizer the entity to save
     * @return the persisted entity
     */
    Organizer save(Organizer organizer);

    /**
     * Get all the organizers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Organizer> findAll(Pageable pageable);

    Page<Organizer> findByCurrentUser(Pageable pageable);

    List<Organizer> findByCurrentUser();

    /**
     * Get the "id" organizer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Organizer findOne(Long id);

    Optional<Organizer> findByNameIgnoreCase(String name);

    /**
     * Delete the "id" organizer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the organizer corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Organizer> search(String query, Pageable pageable);

    Page<Organizer> searchByUser(User user, String query, Pageable pageable);
}
