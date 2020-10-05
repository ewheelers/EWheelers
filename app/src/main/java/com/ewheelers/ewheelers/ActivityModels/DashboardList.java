package com.ewheelers.ewheelers.ActivityModels;

import android.graphics.drawable.Drawable;

public class DashboardList {
    private String title;
    private String attr_one;
    private String atrr_two;
    private String attr_one_price;
    private String attr_two_price;
    private Drawable image;

    public DashboardList(Drawable image, String title, String attr_one, String atrr_two, String attr_one_price, String attr_two_price) {
        this.image = image;
        this.title = title;
        this.attr_one = attr_one;
        this.atrr_two = atrr_two;
        this.attr_one_price = attr_one_price;
        this.attr_two_price = attr_two_price;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttr_one() {
        return attr_one;
    }

    public void setAttr_one(String attr_one) {
        this.attr_one = attr_one;
    }

    public String getAtrr_two() {
        return atrr_two;
    }

    public void setAtrr_two(String atrr_two) {
        this.atrr_two = atrr_two;
    }

    public String getAttr_one_price() {
        return attr_one_price;
    }

    public void setAttr_one_price(String attr_one_price) {
        this.attr_one_price = attr_one_price;
    }

    public String getAttr_two_price() {
        return attr_two_price;
    }

    public void setAttr_two_price(String attr_two_price) {
        this.attr_two_price = attr_two_price;
    }
}
