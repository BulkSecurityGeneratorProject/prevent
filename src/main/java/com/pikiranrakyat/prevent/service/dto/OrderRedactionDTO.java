package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.domain.OrderRedaction;
import com.pikiranrakyat.prevent.domain.Redaction;

import java.math.BigDecimal;

public class OrderRedactionDTO {


    private String orderNumber;

    private String title;

    private String content;

    private String note;

    private Boolean accept = Boolean.FALSE;

    private BigDecimal total;

    private Redaction redaction;

    private Events events;


    public OrderRedactionDTO() {
    }

    public OrderRedactionDTO(OrderRedaction o) {
        this(o.getOrderNumber(),
            o.getTitle(),
            o.getContent(),
            o.getNote(),
            o.isAccept(),
            o.getTotal(),
            o.getRedaction(),
            o.getEvents());
    }

    public OrderRedactionDTO(String orderNumber, String title, String content, String note, Boolean accept, BigDecimal total, Redaction redaction, Events events) {
        this.orderNumber = orderNumber;
        this.title = title;
        this.content = content;
        this.note = note;
        this.accept = accept;
        this.total = total;
        this.redaction = redaction;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Redaction getRedaction() {
        return redaction;
    }

    public void setRedaction(Redaction redaction) {
        this.redaction = redaction;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "OrderRedactionDTO{" +
            "orderNumber='" + orderNumber + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", note='" + note + '\'' +
            ", accept=" + accept +
            ", total=" + total +
            ", redaction=" + redaction +
            ", events=" + events +
            '}';
    }
}
