package com.pikiranrakyat.prevent.web.rest.manage;

import com.pikiranrakyat.prevent.service.FileManagerService;
import com.pikiranrakyat.prevent.service.ImageManagerService;
import com.pikiranrakyat.prevent.service.util.MimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.File;

/**
 * Created by aseprojali on 26/10/16.
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class ManageFileResource {

    @Inject
    private FileManagerService fileManagerService;

    @Inject
    private ImageManagerService imageManagerService;

    @RequestMapping(value = "/file/{name}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFile(@PathVariable("name") String name) {
        log.debug("Find file " + name);

        return
            fileManagerService.findOneByName(name)
                .map(upload -> {
                    FileSystemResource isr = new FileSystemResource(new File(upload.getPath()));
                    HttpHeaders respHeaders = new HttpHeaders();
                    respHeaders.setContentType(MediaType.parseMediaType(MimeUtil.getMimeType(upload.getExtension())));
                    respHeaders.setContentLength(upload.getSize());
                    respHeaders.setContentDispositionFormData("attachment", upload.getName());
                    respHeaders.set("filename", upload.getName() + "." + upload.getExtension());
                    log.debug("Header " + respHeaders.toString());
                    return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }

    @RequestMapping(value = "/image/{name}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getImage(@PathVariable("name") String name) {
        log.debug("Find file " + name);

        return
            imageManagerService.findOneByName(name)
                .map(upload -> {
                    FileSystemResource isr = new FileSystemResource(new File(upload.getPath()));
                    HttpHeaders respHeaders = new HttpHeaders();
                    respHeaders.setContentType(MediaType.parseMediaType(MimeUtil.getMimeType(upload.getExtension())));
                    respHeaders.setContentLength(upload.getSize());
                    respHeaders.setContentDispositionFormData("attachment", upload.getName());
                    respHeaders.set("filename", upload.getName() + "." + upload.getExtension());
                    log.debug("Header " + respHeaders.toString());
                    return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }


}
