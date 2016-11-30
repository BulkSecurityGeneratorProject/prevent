package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.OrderRedaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderRedaction entity.
 */
@SuppressWarnings("unused")
public interface OrderRedactionRepository extends JpaRepository<OrderRedaction,Long> {

}
