package com.pikiranrakyat.prevent.domain;

import com.pikiranrakyat.prevent.domain.master.Merchandise;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OrderMerchandise.
 */
@Entity
@Table(name = "order_merchandise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ordermerchandise")
public class OrderMerchandise extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @NotNull
    @Column(name = "accept", nullable = false)
    private Boolean accept;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @NotNull
    private Merchandise merchandise;

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

    public OrderMerchandise orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean isAccept() {
        return accept;
    }

    public OrderMerchandise accept(Boolean accept) {
        this.accept = accept;
        return this;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getNote() {
        return note;
    }

    public OrderMerchandise note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public OrderMerchandise merchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
        return this;
    }

    public void setMerchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
    }

    public Events getEvents() {
        return events;
    }

    public OrderMerchandise events(Events events) {
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
        OrderMerchandise orderMerchandise = (OrderMerchandise) o;
        if(orderMerchandise.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderMerchandise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderMerchandise{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            ", accept='" + accept + "'" +
            ", note='" + note + "'" +
            '}';
    }
}
