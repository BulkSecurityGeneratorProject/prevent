package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Locations entity.
 */
@SuppressWarnings("unused")
public interface LocationsRepository extends JpaRepository<Locations, Long> {

    Optional<Locations> findByNameIgnoreCase(String name);

    Optional<Locations> findByName(String name);

    Optional<Locations> findByNameAndAddress(String name, String address);

    @Query("SELECT u.name FROM Locations u WHERE u.name LIKE CONCAT('%',:name,'%')")
    List<Locations> findLocationLikeName(@Param("name") String name);
}
