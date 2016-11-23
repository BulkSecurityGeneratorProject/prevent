package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.master.Redaction;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Redaction entity.
 */
@SuppressWarnings("unused")
public interface RedactionRepository extends JpaRepository<Redaction,Long> {

}
