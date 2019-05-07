package de.escalon.hypermedia.sample.springboot.beans.store;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Dietrich on 02.11.2015.
 */
public class OrderedItem extends ResourceSupport {
    private String orderItemNumber;
    private Product orderedItem;

    public Product getOrderedItem() {
        return orderedItem;
    }

    public void setOrderedItem(Product orderedItem) {
        this.orderedItem = orderedItem;
    }

    public String getOrderItemNumber() {
        return orderItemNumber;
    }

    public void setOrderItemNumber(String orderItemNumber) {
        this.orderItemNumber = orderItemNumber;
    }
}
