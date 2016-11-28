package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.Events;
import com.pikiranrakyat.prevent.domain.OrderMerchandise;
import com.pikiranrakyat.prevent.domain.master.Merchandise;

/**
 * A DTO representing a user, with his authorities.
 */
public class OrderMerchandiseDTO {


    private String orderNumber;

    private Boolean accept = Boolean.FALSE;

    private String note;

    private Merchandise merchandise;

    private Events events;

    public OrderMerchandiseDTO() {
    }

    public OrderMerchandiseDTO(OrderMerchandise o) {
        this( o.getOrderNumber(),o.isAccept(),o.getNote(),o.getMerchandise(),o.getEvents());
    }

    public OrderMerchandiseDTO( String orderNumber, Boolean accept, String note, Merchandise merchandise, Events events) {
        this.orderNumber = orderNumber;
        this.accept = accept;
        this.note = note;
        this.merchandise = merchandise;
        this.events = events;
    }



    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "OrderMerchandiseDTO{" +
            ", orderNumber='" + orderNumber + '\'' +
            ", accept=" + accept +
            ", note='" + note + '\'' +
            ", merchandise=" + merchandise +
            ", events=" + events +
            '}';
    }
}
