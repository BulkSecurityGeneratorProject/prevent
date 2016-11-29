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
import java.time.LocalDate;
import java.util.Objects;

/**
 * A OrderAds.
 */
@Entity
@Table(name = "order_ads")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orderads")
public class OrderAds extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @NotNull
    @Size(min = 5, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @NotNull
    @Size(min = 10, max = 200)
    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @Column(name = "note")
    private String note;

    @NotNull
    @Column(name = "accept", nullable = false)
    private Boolean accept;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @DecimalMin(value = "0")
    @Column(name = "total", precision = 30, scale = 2, nullable = false)
    private BigDecimal total;

    @ManyToOne
    @NotNull
    private Ads ads;

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

    public OrderAds orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTitle() {
        return title;
    }

    public OrderAds title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public OrderAds content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public OrderAds note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isAccept() {
        return accept;
    }

    public OrderAds accept(Boolean accept) {
        this.accept = accept;
        return this;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public OrderAds publishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public OrderAds total(BigDecimal total) {
        this.total = total;
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Ads getAds() {
        return ads;
    }

    public OrderAds ads(Ads ads) {
        this.ads = ads;
        return this;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }

    public Events getEvents() {
        return events;
    }

    public OrderAds events(Events events) {
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
        OrderAds orderAds = (OrderAds) o;
        if (orderAds.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderAds.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderAds{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", note='" + note + "'" +
            ", accept='" + accept + "'" +
            ", publishDate='" + publishDate + "'" +
            ", total='" + total + "'" +
            '}';
    }
}
