package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.domain.OrderAds;
import com.pikiranrakyat.prevent.domain.Ads;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderAdsDTO {


    private String orderNumber;

    private String title;

    private String content;

    private String note;

    private Boolean accept = Boolean.FALSE;

    private LocalDate publishDate;

    private BigDecimal total;

    private Ads ads;

    private Events events;


    public OrderAdsDTO() {
    }

    public OrderAdsDTO(OrderAds o) {
        this(o.getOrderNumber(),
            o.getTitle(),
            o.getContent(),
            o.getNote(),
            o.isAccept(),
            o.getPublishDate(),
            o.getTotal(),
            o.getAds(),
            o.getEvents());
    }

    public OrderAdsDTO(String orderNumber, String title, String content, String note, Boolean accept, LocalDate publishDate, BigDecimal total, Ads ads, Events events) {
        this.orderNumber = orderNumber;
        this.title = title;
        this.content = content;
        this.note = note;
        this.accept = accept;
        this.publishDate = publishDate;
        this.total = total;
        this.ads = ads;
        this.events = events;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    @Override
    public String toString() {
        return "OrderAdsDTO{" +
            "orderNumber='" + orderNumber + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", note='" + note + '\'' +
            ", accept=" + accept +
            ", publishDate=" + publishDate +
            ", total=" + total +
            ", ads=" + ads +
            ", events=" + events +
            '}';
    }
}
