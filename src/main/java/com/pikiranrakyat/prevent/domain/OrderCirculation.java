package com.pikiranrakyat.prevent.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
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
    @Min(value = 0)
    @Column(name = "qty", nullable = false)
    private Integer qty;

    @DecimalMin(value = "0")
    @Column(name = "total", precision = 30, scale = 2, nullable = false)
    private BigDecimal total;

    @NotNull
    @Column(name = "accept", nullable = false)
    private Boolean accept;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @NotNull
    private Circulation circulation;

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

    public Circulation getCirculation() {
        return circulation;
    }

    public OrderCirculation circulation(Circulation circulation) {
        this.circulation = circulation;
        return this;
    }

    public void setCirculation(Circulation circulation) {
        this.circulation = circulation;
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


    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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
            ", orderNumber='" + orderNumber + '\'' +
            ", qty=" + qty +
            ", total=" + total +
            ", accept=" + accept +
            ", note='" + note + '\'' +
            ", circulation=" + circulation +
            ", events=" + events +
            '}';
    }
}
