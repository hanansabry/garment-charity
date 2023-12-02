package com.app.garmentcharity.data.models;

import com.app.garmentcharity.data.Item;

public class RequestItem {

    private String id;
    private Item item;
    private double quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
