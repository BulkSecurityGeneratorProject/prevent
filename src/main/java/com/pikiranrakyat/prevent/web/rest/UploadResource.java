package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.io.Files;
import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.repository.UserRepository;
import com.pikiranrakyat.prevent.security.SecurityUtils;
import com.pikiranrakyat.prevent.service.FileManagerService;
import com.pikiranrakyat.prevent.web.rest.vm.UploadVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by aseprojali on 26/10/16.
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class UploadResource {


    @Value("${upload.path.file}")
    String pathFile;

    @Inject
    private UserRepository userRepository;

    @Inject
    private FileManagerService fileManagerService;

    @RequestMapping(value = "/upload",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> postUpload(@RequestParam("file") MultipartFile multipartFile) {

        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        // Get original filename
        String originalFilename = multipartFile.getOriginalFilename();

        log.debug("originalFilename " + originalFilename);

        // Get file extension
        String fileExtension = Files.getFileExtension(originalFilename);
        log.debug("fileExtension " + fileExtension);

        // Create random file name
        String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
        log.debug("newFileName " + newFileName);

        // Get full path
        String filePath = Paths.get(pathFile, newFileName).toString();
        log.debug("filePath " + filePath);

        // Get size file
        long size = multipartFile.getSize();
        log.debug("file size  " + size);

        // New File
        File file = new File(filePath);

        try {

            // Create file to full path
            BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(file));
            stream.write(multipartFile.getBytes());
            stream.close();

            FileManager fileManager = new FileManager();
            fileManager.setOriginal(originalFilename);
            fileManager.setName(Files.getNameWithoutExtension(file.getAbsolutePath()));
            fileManager.setPath(filePath);
            fileManager.setExtension(fileExtension);
            fileManager.setSize(size);
            fileManager.setUser(user.get());

            FileManager saved = fileManagerService.save(fileManager);

            return new ResponseEntity<>(new UploadVM(saved), HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error upload file " + e.getMessage());
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
