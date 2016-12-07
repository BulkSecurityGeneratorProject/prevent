package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.*;

import java.math.BigDecimal;

/**
 * A DTO representing a user, with his authorities.
 */
public class OrderCiculationDTO {


    private String orderNumber;


    private Boolean accept = Boolean.FALSE;

    private String note;

    private Circulation circulation;

    private Events events;

    private Integer qty;

    private BigDecimal total;

    public OrderCiculationDTO() {
    }

    public OrderCiculationDTO(OrderCirculation o) {
        this(o.getOrderNumber(),
            o.isAccept(),
            o.getNote(),
            o.getCirculation(),
            o.getEvents(),
            o.getQty(),
            o.getTotal());
    }

    public OrderCiculationDTO(String orderNumber,
                              Boolean accept, String note, Circulation circulation,
                              Events events, Integer qty, BigDecimal total) {
        this.orderNumber = orderNumber;
        this.accept = accept;
        this.note = note;
        this.circulation = circulation;
        this.events = events;
        this.qty = qty;
        this.total = total;
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

    public Circulation getCirculation() {
        return circulation;
    }

    public void setCirculation(Circulation circulation) {
        this.circulation = circulation;
    }

    public Events getEvents() {
        return events;
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
    public String toString() {
        return "OrderCiculationDTO{" +
            "orderNumber='" + orderNumber + '\'' +
            ", accept=" + accept +
            ", note='" + note + '\'' +
            ", circulation=" + circulation +
            ", events=" + events +
            ", qty=" + qty +
            ", total=" + total +
            '}';
    }
}
