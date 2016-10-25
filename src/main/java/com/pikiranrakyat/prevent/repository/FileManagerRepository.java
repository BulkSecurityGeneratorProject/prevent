package com.pikiranrakyat.prevent.repository;

import com.pikiranrakyat.prevent.domain.FileManager;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the FileManager entity.
 */
@SuppressWarnings("unused")
public interface FileManagerRepository extends JpaRepository<FileManager, Long> {

    @Query("select fileManager from FileManager fileManager where fileManager.user.login = ?#{principal.username}")
    List<FileManager> findByUserIsCurrentUser();


    Optional<FileManager> findOneByName(String name);

}
