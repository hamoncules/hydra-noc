package de.escalon.hypermedia.sample.springboot.beans.store;

import de.escalon.hypermedia.hydra.mapping.Expose;
import de.escalon.hypermedia.sample.springboot.model.store.OrderStatus;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dietrich on 17.02.2015.
 */
public class Order extends ResourceSupport {
    private List<Product> items = new ArrayList<Product>();
    private Offer acceptedOffer;
    private Store seller;
    private OrderStatus orderStatus;

    public void setSeller(Store seller) {
        this.seller = seller;
    }

    public Store getSeller() {
        return seller;
    }


    public void addItem(Product orderedItem) {
        this.items.add(orderedItem);
    }

    @Expose("orderedItem")
    public List<? extends Product> getItems() {
        return items;
    }


    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
