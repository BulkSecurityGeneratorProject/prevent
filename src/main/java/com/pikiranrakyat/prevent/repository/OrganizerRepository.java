package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.EventType;
import com.pikiranrakyat.prevent.domain.Organizer;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Organizer entity.
 */
@SuppressWarnings("unused")
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    @Query("select organizer from Organizer organizer where organizer.user.login = ?#{principal.username}")
    List<Organizer> findByUserIsCurrentUser();

    Optional<Organizer> findByNameIgnoreCase(String name);

    @Query("SELECT organizer FROM Organizer organizer WHERE organizer.name LIKE CONCAT('%',:name,'%') AND organizer.user.login = ?#{principal.username}")
    List<Organizer> findOrganizerLikeNameWhereUserLogin(@Param("name") String name);
}
