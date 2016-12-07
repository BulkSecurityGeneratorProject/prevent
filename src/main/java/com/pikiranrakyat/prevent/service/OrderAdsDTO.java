package com.pikiranrakyat.prevent.service;

import com.pikiranrakyat.prevent.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO representing a user, with his authorities.
 */
public class OrderAdsDTO {


    private String orderNumber;

    private String title;

    private String content;

    private Boolean accept = Boolean.FALSE;

    private LocalDate publishDate;

    private String note;

    private Ads ads;

    private Events events;

    private BigDecimal total;

    public OrderAdsDTO() {
    }

    public OrderAdsDTO(OrderAds o) {
        this(o.getOrderNumber(),
            o.getTitle(),
            o.getContent(),
            o.isAccept(),
            o.getPublishDate(),
            o.getNote(),
            o.getAds(),
            o.getEvents(),
            o.getTotal());
    }

    public OrderAdsDTO(String orderNumber, String title, String content, Boolean accept, LocalDate publishDate, String note, Ads ads, Events events, BigDecimal total) {
        this.orderNumber = orderNumber;
        this.title = title;
        this.content = content;
        this.accept = accept;
        this.publishDate = publishDate;
        this.note = note;
        this.ads = ads;
        this.events = events;
        this.total = total;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderAdsDTO{" +
            "orderNumber='" + orderNumber + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", accept=" + accept +
            ", publishDate=" + publishDate +
            ", note='" + note + '\'' +
            ", ads=" + ads +
            ", events=" + events +
            ", total=" + total +
            '}';
    }
}
