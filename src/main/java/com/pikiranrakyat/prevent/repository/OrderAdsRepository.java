package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.OrderAds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderAds entity.
 */
@SuppressWarnings("unused")
public interface OrderAdsRepository extends JpaRepository<OrderAds, Long> {

    List<OrderAds> findByEventsId(Long eventId);

}
