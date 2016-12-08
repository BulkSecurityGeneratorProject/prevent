package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created by aseprojali on 30/11/16.
 */
public class EventsDTO {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private ZonedDateTime starts;

    @NotNull
    private ZonedDateTime ends;

    @NotNull
    private String locationName;

    @NotNull
    private String locationAddress;

    private Double locationLatitude;

    private Double locationLongitude;

    private BigDecimal subtotal = BigDecimal.ZERO;

    private Boolean accept = Boolean.FALSE;

    private String note;

    private Boolean isOrder = Boolean.FALSE;

    private EventType eventType;

    private Organizer organizer;

    private ImageManager image;

    private FileManager file;


    public EventsDTO() {
    }

    public EventsDTO(String title,
                     String description,
                     ZonedDateTime starts,
                     ZonedDateTime ends,
                     String locationName,
                     String locationAddress,
                     Double locationLatitude,
                     Double locationLongitude,
                     BigDecimal subtotal,
                     Boolean accept,
                     String note,
                     Boolean isOrder,
                     EventType eventType,
                     Organizer organizer,
                     ImageManager image,
                     FileManager file) {
        this.title = title;
        this.description = description;
        this.starts = starts;
        this.ends = ends;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.subtotal = subtotal;
        this.accept = accept;
        this.note = note;
        this.isOrder = isOrder;
        this.eventType = eventType;
        this.organizer = organizer;
        this.image = image;
        this.file = file;
    }

    public EventsDTO(Events o) {
        this(
            o.getTitle(),
            o.getDescription(),
            o.getStarts(),
            o.getEnds(),
            o.getLocationName(),
            o.getLocationAddress(),
            o.getLocationLatitude(),
            o.getLocationLongitude(),
            o.getSubtotal(),
            o.isAccept(),
            o.getNote(),
            o.isIsOrder(),
            o.getEventType(),
            o.getOrganizer(),
            o.getImage(),
            o.getFile()

        );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStarts() {
        return starts;
    }

    public void setStarts(ZonedDateTime starts) {
        this.starts = starts;
    }

    public ZonedDateTime getEnds() {
        return ends;
    }

    public void setEnds(ZonedDateTime ends) {
        this.ends = ends;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getOrder() {
        return isOrder;
    }

    public void setOrder(Boolean order) {
        isOrder = order;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public ImageManager getImage() {
        return image;
    }

    public void setImage(ImageManager image) {
        this.image = image;
    }

    public FileManager getFile() {
        return file;
    }

    public void setFile(FileManager file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "EventsDTO{" +
            "title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", starts=" + starts +
            ", ends=" + ends +
            ", locationName='" + locationName + '\'' +
            ", locationAddress='" + locationAddress + '\'' +
            ", locationLatitude=" + locationLatitude +
            ", locationLongitude=" + locationLongitude +
            ", subtotal=" + subtotal +
            ", accept=" + accept +
            ", note='" + note + '\'' +
            ", isOrder=" + isOrder +
            ", eventType=" + eventType +
            ", organizer=" + organizer +
            ", image=" + image +
            ", file=" + file +
            '}';
    }
}
