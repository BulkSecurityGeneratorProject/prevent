package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.OrderCirculation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderCirculation entity.
 */
@SuppressWarnings("unused")
public interface OrderCirculationRepository extends JpaRepository<OrderCirculation,Long> {

    List<OrderCirculation> findByEventsId(Long eventId);

}
