package com.pikiranrakyat.prevent.service;

import com.google.common.io.Files;
import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.domain.ImageManager;
import com.pikiranrakyat.prevent.domain.User;
import com.pikiranrakyat.prevent.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by aseprojali on 15/12/16.
 */
@Service
@Slf4j
public class UploadService {

    @Value("${upload.path.file}")
    String pathFile;

    @Value("${upload.path.image}")
    String pathImage;

    @Inject
    private UserRepository userRepository;


    public ImageManager setImage(MultipartFile multipartFile, String userLogin) throws IOException {

        Optional<User> user = userRepository.findOneByLogin(userLogin);

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
        String filePath = Paths.get(pathImage, newFileName).toString();
        log.debug("filePath " + filePath);

        // Get size file
        long size = multipartFile.getSize();
        log.debug("file size  " + size);

        // New File
        File file = new File(filePath);

        // Create file to full path
        BufferedOutputStream stream =
            new BufferedOutputStream(new FileOutputStream(file));
        stream.write(multipartFile.getBytes());
        stream.close();


        ImageManager imageManager = new ImageManager();
        imageManager.setOriginal(originalFilename);
        imageManager.setName(Files.getNameWithoutExtension(file.getAbsolutePath()));
        imageManager.setPath(filePath);
        imageManager.setExtension(fileExtension);
        imageManager.setSize(size);
        imageManager.setUser(user.get());

        return imageManager;
    }

    public FileManager setFile(MultipartFile multipartFile, String userLogin) throws IOException {

        Optional<User> user = userRepository.findOneByLogin(userLogin);

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

        return fileManager;
    }

}
