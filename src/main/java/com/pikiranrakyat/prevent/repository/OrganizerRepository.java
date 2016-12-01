package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.Organizer;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Organizer entity.
 */
@SuppressWarnings("unused")
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    @Query("select organizer from Organizer organizer where organizer.user.login = ?#{principal.username}")
    List<Organizer> findByUserIsCurrentUser();

    Optional<Organizer> findByName(String name);

}
