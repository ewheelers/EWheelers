package com.ewheelers.ewheelers.ActivityModels;

public class InventoryModel {
    private String name;
    private String sell_price;
    private String avail_qty;
    private String rental_qty;
    private String statusOfprod;

    public InventoryModel(String name, String sell_price, String avail_qty, String rental_qty) {
        this.name = name;
        this.sell_price = sell_price;
        this.avail_qty = avail_qty;
        this.rental_qty = rental_qty;
    }

    public InventoryModel() {

    }

    public String getStatusOfprod() {
        return statusOfprod;
    }

    public void setStatusOfprod(String statusOfprod) {
        this.statusOfprod = statusOfprod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getAvail_qty() {
        return avail_qty;
    }

    public void setAvail_qty(String avail_qty) {
        this.avail_qty = avail_qty;
    }

    public String getRental_qty() {
        return rental_qty;
    }

    public void setRental_qty(String rental_qty) {
        this.rental_qty = rental_qty;
    }
}
