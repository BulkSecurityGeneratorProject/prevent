package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.OrderMerchandise;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderMerchandise entity.
 */
@SuppressWarnings("unused")
public interface OrderMerchandiseRepository extends JpaRepository<OrderMerchandise,Long> {

}
