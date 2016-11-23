package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.master.Merchandise;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Merchandise entity.
 */
@SuppressWarnings("unused")
public interface MerchandiseRepository extends JpaRepository<Merchandise,Long> {

}
