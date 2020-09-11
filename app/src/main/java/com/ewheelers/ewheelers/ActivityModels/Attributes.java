package com.ewheelers.ewheelers.ActivityModels;

import android.content.Context;

import org.json.JSONArray;

public class Attributes {
    private String productid;
    private String productname;
    private String producttype;
    private JSONArray jsonarray;

    public Attributes(String productid, String productname, String producttype) {
        this.productid = productid;
        this.productname = productname;
        this.producttype = producttype;
    }

    public Attributes(JSONArray jsonarray) {
        this.jsonarray = jsonarray;
    }

    public Attributes() {

    }

    public JSONArray getJsonarray() {
        return jsonarray;
    }

    public void setJsonarray(JSONArray jsonarray) {
        this.jsonarray = jsonarray;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }
}
