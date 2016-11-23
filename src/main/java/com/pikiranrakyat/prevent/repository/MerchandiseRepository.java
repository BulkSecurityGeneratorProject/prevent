package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.Merchandise;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Merchandise entity.
 */
@SuppressWarnings("unused")
public interface MerchandiseRepository extends JpaRepository<Merchandise,Long> {

}
