package com.pikiranrakyat.prevent.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OrderCirculation.
 */
@Entity
@Table(name = "order_circulation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ordercirculation")
public class OrderCirculation extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @NotNull
    @Size(min = 5, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @NotNull
    @Size(min = 10, max = 200)
    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @NotNull
    @Column(name = "accept", nullable = false)
    private Boolean accept;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @NotNull
    private Redaction redaction;

    @ManyToOne
    @NotNull
    private Events events;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public OrderCirculation orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTitle() {
        return title;
    }

    public OrderCirculation title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public OrderCirculation content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isAccept() {
        return accept;
    }

    public OrderCirculation accept(Boolean accept) {
        this.accept = accept;
        return this;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getNote() {
        return note;
    }

    public OrderCirculation note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Redaction getRedaction() {
        return redaction;
    }

    public OrderCirculation redaction(Redaction redaction) {
        this.redaction = redaction;
        return this;
    }

    public void setRedaction(Redaction redaction) {
        this.redaction = redaction;
    }

    public Events getEvents() {
        return events;
    }

    public OrderCirculation events(Events events) {
        this.events = events;
        return this;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderCirculation orderCirculation = (OrderCirculation) o;
        if (orderCirculation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderCirculation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderCirculation{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", accept='" + accept + "'" +
            ", note='" + note + "'" +
            '}';
    }
}
