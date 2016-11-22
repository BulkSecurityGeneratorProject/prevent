package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.ContactPerson;
import com.pikiranrakyat.prevent.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ContactPerson entity.
 */
@SuppressWarnings("unused")
public interface ContactPersonRepository extends JpaRepository<ContactPerson,Long> {

    Page<ContactPerson> findAllByUser(User user, Pageable pageable);
}
