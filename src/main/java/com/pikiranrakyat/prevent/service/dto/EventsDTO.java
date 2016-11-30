package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.EventType;
import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.domain.Locations;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created by aseprojali on 30/11/16.
 */
public class EventsDTO {

    private String title;

    private String description;

    private ZonedDateTime starts;

    private ZonedDateTime ends;

    private BigDecimal subtotal;

    private Boolean accept;

    private String note;

    private Boolean isOrder;

    private EventType eventType;

    private Locations locations;

    public EventsDTO() {
    }

    public EventsDTO(String title, String description, ZonedDateTime starts, ZonedDateTime ends, BigDecimal subtotal, Boolean accept, String note, Boolean isOrder, EventType eventType, Locations locations) {
        this.title = title;
        this.description = description;
        this.starts = starts;
        this.ends = ends;
        this.subtotal = subtotal;
        this.accept = accept;
        this.note = note;
        this.isOrder = isOrder;
        this.eventType = eventType;
        this.locations = locations;
    }

    public EventsDTO(Events o) {
        this(
            o.getTitle(),
            o.getDescription(),
            o.getStarts(),
            o.getEnds(),
            o.getSubtotal(),
            o.isAccept(),
            o.getNote(),
            o.isIsOrder(),
            o.getEventType(),
            o.getLocations()
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

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "EventsDTO{" +
            "title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", starts=" + starts +
            ", ends=" + ends +
            ", subtotal=" + subtotal +
            ", accept=" + accept +
            ", note='" + note + '\'' +
            ", isOrder=" + isOrder +
            ", eventType=" + eventType +
            ", locations=" + locations +
            '}';
    }
}
