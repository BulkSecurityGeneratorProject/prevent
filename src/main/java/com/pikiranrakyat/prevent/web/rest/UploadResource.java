package com.pikiranrakyat.prevent.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.domain.ImageManager;
import com.pikiranrakyat.prevent.security.SecurityUtils;
import com.pikiranrakyat.prevent.service.FileManagerService;
import com.pikiranrakyat.prevent.service.ImageManagerService;
import com.pikiranrakyat.prevent.service.UploadService;
import com.pikiranrakyat.prevent.web.rest.vm.UploadVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by aseprojali on 26/10/16.
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class UploadResource {

    @Inject
    private UploadService uploadService;

    @Inject
    private FileManagerService fileManagerService;

    @Inject
    private ImageManagerService imageManagerService;

    @RequestMapping(value = "/upload/file",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> postUploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            // Create file to full path
            FileManager fileManager = uploadService.setFile(multipartFile, SecurityUtils.getCurrentUserLogin());
            FileManager saved = fileManagerService.save(fileManager);

            return new ResponseEntity<>(new UploadVM(saved), HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error upload file " + e.getMessage());
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/upload/image",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> postUploadImage(@RequestParam("image") MultipartFile multipartFile) {
        try {
            // Create file to full path
            ImageManager imageManager = uploadService.setImage(multipartFile, SecurityUtils.getCurrentUserLogin());
            ImageManager saved = imageManagerService.save(imageManager);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error upload file " + e.getMessage());
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
