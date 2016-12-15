package com.pikiranrakyat.prevent.service;

import com.google.common.base.Charsets;
import com.pikiranrakyat.prevent.PreventApp;
import com.pikiranrakyat.prevent.config.Constants;
import com.pikiranrakyat.prevent.domain.Events;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PreventApp.class)
@Transactional
public class IntegrasiFileServiceIntTest {

    @Value("${existing.path.image}")
    String pathExistringFile;

    @Inject
    private EventsService eventsService;


    private static final String PATH_FILE_CSV = "/Users/aseprojali/Workspace/work/backend/prevent/src/test/resources/csv/events-image.csv";

    @Test
    public void getDirectory() throws IOException {

        FileUtils
            .readLines(new File(PATH_FILE_CSV), Charsets.UTF_8)
            .stream()
            .map(s -> s.split(Constants.DELIMETER_CSV))
            .forEach(contents -> {
                System.out.println(contents[0]);
                Optional<Events> events = eventsService.searchTitleIgnoreCase(contents[0]);

                if (events.isPresent()) {
                    Events event = events.get();
                    File file = new File(pathExistringFile + contents[1]);
                    FileInputStream input = null;
                    try {
                        input = new FileInputStream(file);
                        MultipartFile multipartFile = new MockMultipartFile(contents[1],
                            contents[1], contents[2], IOUtils.toByteArray(input));
                        System.out.println(multipartFile.getName());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("event tidak ada");
                }

            });

    }
}
