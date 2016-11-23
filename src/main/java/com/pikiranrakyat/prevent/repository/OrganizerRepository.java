package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.Organizer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Organizer entity.
 */
@SuppressWarnings("unused")
public interface OrganizerRepository extends JpaRepository<Organizer,Long> {

    @Query("select organizer from Organizer organizer where organizer.user.login = ?#{principal.username}")
    List<Organizer> findByUserIsCurrentUser();

}
