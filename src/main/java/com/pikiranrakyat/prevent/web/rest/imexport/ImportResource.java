package com.pikiranrakyat.prevent.web.rest.imexport;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.pikiranrakyat.prevent.config.Constants;
import com.pikiranrakyat.prevent.domain.*;
import com.pikiranrakyat.prevent.exception.DataNotFoundException;
import com.pikiranrakyat.prevent.repository.UserRepository;
import com.pikiranrakyat.prevent.service.EventTypeService;
import com.pikiranrakyat.prevent.service.EventsService;
import com.pikiranrakyat.prevent.service.LocationsService;
import com.pikiranrakyat.prevent.service.OrganizerService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by aseprojali on 01/12/16.
 */

@RestController
@RequestMapping("/api")
public class ImportResource {

    private final Logger log = LoggerFactory.getLogger(ImportResource.class);

    @Value("${upload.path.import}")
    String pathFileImport;

    @Inject
    private UserRepository userRepository;

    @Inject
    private OrganizerService organizerService;

    @Inject
    private EventTypeService eventTypeService;

    @Inject
    private LocationsService locationsService;

    @Inject
    private EventsService eventsService;


    @RequestMapping(value = "/import",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void postImport(
        @RequestParam(value = "type") String type,
        @RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        log.debug("REST request import " + type);

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
        String filePath = Paths.get(pathFileImport, newFileName).toString();
        log.debug("filePath " + filePath);

        // Get size file
        long size = multipartFile.getSize();
        log.debug("file size  " + size);

        // New File
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        // Create file to full path
        BufferedOutputStream stream =
            new BufferedOutputStream(new FileOutputStream(file));
        stream.write(multipartFile.getBytes());
        stream.close();


        switch (type) {
            case Constants.IMPORT.ORGANIZER:
                List<Organizer> organizers = FileUtils.readLines(file, Charsets.UTF_8)
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
                break;
            case Constants.IMPORT.EVENT:

                List<Events> events = FileUtils.readLines(file, Charsets.UTF_8)
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


                        Locations locations = locationsService
                            .findByNameAndAddress(contents[8], contents[9])
                            .orElseGet(() -> {
                                Locations location = new Locations();
                                location.setName(contents[8]);
                                location.setAddress(contents[9]);
                                location.setCity("");
                                location.setState("");
                                location.setPostalCode("00000");
                                location.setLatitude(0.0);
                                location.setLongitude(0.0);
                                return locationsService.save(location);
                            });

                        event.setLocations(locations);

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

                events.forEach(event -> {
                    eventsService.save(event);
                });


                break;

        }
    }

    public static ZonedDateTime convertToZoneDateTime(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDate date = LocalDate.parse(fecha, formatter);
        return date.atStartOfDay(ZoneId.of("Asia/Jakarta"));
    }
}
