package com.pikiranrakyat.prevent.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Events.
 */
@Entity
@Table(name = "events")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "events")
public class Events extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 6, max = 50)
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @NotNull
    @Size(min = 10, max = 300)
    @Column(name = "description", length = 300, nullable = false)
    private String description;

    @NotNull
    @Column(name = "starts", nullable = false)
    private ZonedDateTime starts;

    @NotNull
    @Column(name = "ends", nullable = false)
    private ZonedDateTime ends;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @NotNull
    @Column(name = "accept", nullable = false)
    private Boolean accept;

    @Column(name = "note")
    private String note;

    @NotNull
    @Column(name = "is_order", nullable = false)
    private Boolean isOrder;

    @ManyToOne
    @NotNull
    private EventType eventType;

    @ManyToOne
    @NotNull
    private Locations locations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Events title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Events description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStarts() {
        return starts;
    }

    public Events starts(ZonedDateTime starts) {
        this.starts = starts;
        return this;
    }

    public void setStarts(ZonedDateTime starts) {
        this.starts = starts;
    }

    public ZonedDateTime getEnds() {
        return ends;
    }

    public Events ends(ZonedDateTime ends) {
        this.ends = ends;
        return this;
    }

    public void setEnds(ZonedDateTime ends) {
        this.ends = ends;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public Events subtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Boolean isAccept() {
        return accept;
    }

    public Events accept(Boolean accept) {
        this.accept = accept;
        return this;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getNote() {
        return note;
    }

    public Events note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isIsOrder() {
        return isOrder;
    }

    public Events isOrder(Boolean isOrder) {
        this.isOrder = isOrder;
        return this;
    }

    public void setIsOrder(Boolean isOrder) {
        this.isOrder = isOrder;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Events eventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Locations getLocations() {
        return locations;
    }

    public Events locations(Locations locations) {
        this.locations = locations;
        return this;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Events events = (Events) o;
        if (events.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, events.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Events{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", starts='" + starts + "'" +
            ", ends='" + ends + "'" +
            ", subtotal='" + subtotal + "'" +
            ", accept='" + accept + "'" +
            ", note='" + note + "'" +
            ", isOrder='" + isOrder + "'" +
            '}';
    }
}
