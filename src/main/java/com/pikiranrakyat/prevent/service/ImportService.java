package com.pikiranrakyat.prevent.service;

import com.google.common.base.Charsets;
import com.pikiranrakyat.prevent.config.Constants;
import com.pikiranrakyat.prevent.domain.*;
import com.pikiranrakyat.prevent.exception.DataNotFoundException;
import com.pikiranrakyat.prevent.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by aseprojali on 14/12/16.
 */
@Service
@Slf4j
public class ImportService {

    @Value("${existing.path.image}")
    String pathExistringImage;

    @Value("${existing.path.file}")
    String pathExistingFile;

    @Inject
    private EventTypeService eventTypeService;

    @Inject
    private OrganizerService organizerService;

    @Inject
    private EventsService eventsService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UploadService uploadService;

    @Inject
    private ImageManagerService imageManagerService;

    @Inject
    private FileManagerService fileManagerService;

    @Async
    public void saveEventsImport(File file) throws IOException {
        List<Events> events = FileUtils
            .readLines(file, Charsets.UTF_8)
            .stream()
            .map(s -> s.split(Constants.DELIMETER_CSV))
            .map(contents -> {

                Events event = new Events();
                event.setTitle(contents[1]);
                event.setDescription(contents[2]);
                event.setStarts(convertToZoneDateTime(contents[3]));
                event.setEnds(convertToZoneDateTime(contents[4]));
                event.setAgreeDate(convertToZoneDateTime(contents[5]));
                event.setAccept(Boolean.valueOf(contents[6]));

                EventType eventType = eventTypeService
                    .findByNameIgnoreCase(contents[7])
                    .orElseThrow(() -> new DataNotFoundException("Event type tidak ada " + contents[7]));
                event.setEventType(eventType);

                event.setLocationName(contents[8]);
                event.setLocationAddress(contents[9]);
                event.setLocationLatitude(Double.valueOf(contents[15]));
                event.setLocationLongitude(Double.valueOf(contents[16]));

                event.setCreatedBy(contents[11]);
                event.setCreatedDate(convertToZoneDateTime(contents[12]));
                event.setLastModifiedBy(contents[13]);
                event.setLastModifiedDate(convertToZoneDateTime(contents[14]));


                log.debug("Event set " + event.toString());

                Organizer organizer = organizerService.findByNameIgnoreCase(contents[10])
                    .orElseThrow(() -> new DataNotFoundException("Organizer not found " + contents[10]));
                log.debug("Organizer set " + organizer);
                event.setOrganizer(organizer);

                return event;
            }).collect(Collectors.toList());

        events
            .forEach(event -> eventsService.searchTitleIgnoreCase(event.getTitle())
                .orElseGet(() -> eventsService.save(event)));
    }


    @Async
    public void saveEventPrImport(File file, String userLogin) throws IOException {
        User user = userRepository.findOneByLogin(userLogin).get();

        List<Events> events = FileUtils
            .readLines(file, Charsets.UTF_8)
            .stream()
            .skip(1)
            .map(s -> s.split(Constants.DELIMETER_CSV))
            .map(contents -> {
                for (String s : contents) {
                    System.out.println(s);
                }

                Events event = new Events();
                event.setTitle(contents[0]);
                event.setDescription(contents[1]);

                if (!contents[2].isEmpty())
                    event.setStarts(convertToZoneDateTime(contents[2]));
                if (!contents[3].isEmpty())
                    event.setEnds(convertToZoneDateTime(contents[3]));

                EventType eventType = eventTypeService
                    .findByNameIgnoreCase(contents[4])
                    .orElseThrow(() -> new DataNotFoundException("Event type tidak ada " + contents[4]));
                event.setEventType(eventType);

                log.debug("Event " + event);
                event.setLocationName(contents[5]);
                event.setLocationAddress(contents[6]);
                event.setLocationLatitude(Optional.ofNullable(contents[7]).filter(s -> !s.isEmpty()).map(Double::valueOf).orElse(Double.valueOf("0")));
                event.setLocationLongitude(Optional.ofNullable(contents[8]).filter(s -> !s.isEmpty()).map(Double::valueOf).orElse(Double.valueOf("0")));

                Organizer organizer = organizerService
                    .findByNameIgnoreCase(contents[9])
                    .orElseGet(() -> {
                        Organizer o = new Organizer();

                        o.setName(contents[9]);
                        if (!contents[10].isEmpty())
                            o.setAddress(contents[10]);
                        if (!contents[11].isEmpty())
                            o.setPhone(Optional.ofNullable(contents[11]).filter(phone -> !phone.isEmpty()).orElse(""));
                        o.setUser(user);
                        o.setEmail(user.getEmail());

                        return organizerService.save(o);
                    });
                event.setOrganizer(organizer);

                return event;
            }).collect(Collectors.toList());

        events
            .forEach(event -> eventsService.searchTitleIgnoreCase(event.getTitle())
                .orElseGet(() -> eventsService.save(event)));

        log.debug("Event PR " + events.toString());
    }


    @Async
    public void saveEventPrImageImport(File file, String userLogin) throws IOException {

        FileUtils
            .readLines(file, Charsets.UTF_8)
            .stream()
            .map(s -> s.split(Constants.DELIMETER_CSV))
            .forEach(contents -> {
                Optional<Events> events = eventsService.searchTitleIgnoreCase(contents[0]);
                if (events.isPresent()) {

                    Events event = events.get();

                    File imageExist = new File(pathExistringImage + contents[1]);
                    FileInputStream inputImage = null;


                    File fileExist = new File(pathExistingFile + contents[3]);
                    FileInputStream inputFile = null;

                    try {

                        inputImage = new FileInputStream(imageExist);
                        inputFile = new FileInputStream(fileExist);

                        MultipartFile multipartFileImage = new MockMultipartFile(contents[1],
                            contents[1], contents[2], IOUtils.toByteArray(inputImage));

                        MultipartFile multipartFile = new MockMultipartFile(contents[3],
                            contents[3], contents[4], IOUtils.toByteArray(inputFile));

                        ImageManager imageManager = uploadService.setImage(multipartFileImage, userLogin);
                        ImageManager manager = imageManagerService.save(imageManager);

                        FileManager fileManager = uploadService.setFile(multipartFile, userLogin);
                        FileManager updateFileManager = fileManagerService.save(fileManager);

                        event.setImage(manager);
                        event.setFile(updateFileManager);

                        eventsService.save(event);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
    }

    @Async
    public void saveOrganizerImport(File file) throws IOException {
        List<Organizer> organizers = FileUtils
            .readLines(file, Charsets.UTF_8)
            .stream()
            .map(s -> s.split(Constants.DELIMETER_CSV))
            .map(contents -> {
                Organizer organizer = new Organizer();
                organizer.setName(contents[1]);
                organizer.setAddress(contents[2]);
                organizer.setPhone(Optional.of(contents[3]).filter(s -> !s.equalsIgnoreCase("system")).orElse("0000"));
                organizer.setEmail(contents[4]);

                String userLogin = contents[5];
                log.debug("User login " + userLogin);
                User user = userRepository.findOneByLogin(userLogin).orElseThrow(() -> new UsernameNotFoundException("User tidak ada"));
                organizer.setUser(user);

                return organizer;
            })
            .collect(Collectors.toList());

        organizers.forEach(organizer -> {

            log.debug("Organizer " + organizer.toString());

            organizerService.findByNameIgnoreCase(organizer.getName())
                .map(o -> {
                    o.setName(organizer.getName());
                    o.setAddress(organizer.getAddress());
                    o.setPhone(organizer.getPhone());
                    o.setEmail(organizer.getEmail());
                    o.setUser(organizer.getUser());
                    return organizerService.save(o);
                })
                .orElseGet(() -> organizerService.save(organizer));
        });
    }

    private static ZonedDateTime convertToZoneDateTime(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate date = LocalDate.parse(fecha, formatter);
        return date.atStartOfDay(ZoneId.of("Asia/Jakarta"));
    }
}
