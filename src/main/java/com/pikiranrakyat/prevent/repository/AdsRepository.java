package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.master.Ads;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Ads entity.
 */
@SuppressWarnings("unused")
public interface AdsRepository extends JpaRepository<Ads,Long> {

}
