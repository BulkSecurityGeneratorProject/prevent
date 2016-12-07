package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.*;
import com.pikiranrakyat.prevent.web.rest.vm.ManagedOrderMerchandiseVM;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aseprojali on 30/11/16.
 */
public class EventOrderDTO {

    private Long id;

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

    private ZonedDateTime agreeDate;


    private String note;

    private Boolean isOrder = Boolean.FALSE;

    private EventType eventType;

    private Organizer organizer;

    private ImageManager image;

    private FileManager file;

    private List<ManagedOrderMerchandiseVM> orderMerchandises = new ArrayList<>();


    public EventOrderDTO() {
    }

    public EventOrderDTO(Long id, String title, String description, ZonedDateTime starts, ZonedDateTime ends, String locationName, String locationAddress, Double locationLatitude, Double locationLongitude, BigDecimal subtotal, Boolean accept, ZonedDateTime agreeDate, String note, Boolean isOrder, EventType eventType, Organizer organizer, ImageManager image, FileManager file) {
        this.id = id;
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
        this.agreeDate = agreeDate;
        this.note = note;
        this.isOrder = isOrder;
        this.eventType = eventType;
        this.organizer = organizer;
        this.image = image;
        this.file = file;
    }

    public EventOrderDTO(Events events) {
        this(
            events.getId(),
            events.getTitle(),
            events.getDescription(),
            events.getStarts(),
            events.getEnds(),
            events.getLocationName(),
            events.getLocationAddress(),
            events.getLocationLatitude(),
            events.getLocationLongitude(),
            events.getSubtotal(),
            events.isAccept(),
            events.getAgreeDate(),
            events.getNote(),
            events.isIsOrder(),
            events.getEventType(),
            events.getOrganizer(),
            events.getImage(),
            events.getFile()
        );
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<ManagedOrderMerchandiseVM> getOrderMerchandises() {
        return orderMerchandises;
    }

    public void setOrderMerchandises(List<ManagedOrderMerchandiseVM> orderMerchandises) {
        this.orderMerchandises = orderMerchandises;
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

    public ZonedDateTime getAgreeDate() {
        return agreeDate;
    }

    public void setAgreeDate(ZonedDateTime agreeDate) {
        this.agreeDate = agreeDate;
    }

    @Override
    public String toString() {
        return "EventOrderDTO{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
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
            ", orderMerchandises=" + orderMerchandises +
            '}';
    }
}
