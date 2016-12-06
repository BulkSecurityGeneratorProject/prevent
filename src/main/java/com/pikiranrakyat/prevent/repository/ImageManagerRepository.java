package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.domain.ImageManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the FileManager entity.
 */
@SuppressWarnings("unused")
public interface ImageManagerRepository extends JpaRepository<ImageManager, Long> {

    @Query("select imageManager from ImageManager imageManager where imageManager.user.login = ?#{principal.username}")
    List<ImageManager> findByUserIsCurrentUser();

    Optional<ImageManager> findOneByName(String name);

}
