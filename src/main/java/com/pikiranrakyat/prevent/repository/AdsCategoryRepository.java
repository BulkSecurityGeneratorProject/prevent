package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.AdsCategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AdsCategory entity.
 */
@SuppressWarnings("unused")
public interface AdsCategoryRepository extends JpaRepository<AdsCategory,Long> {

}
