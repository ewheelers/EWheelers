package com.ewheelers.ewheelers.ActivityModels;

public class Attributes {
    private String productid;
    private String productname;
    private String producttype;

    public Attributes(String productid, String productname, String producttype) {
        this.productid = productid;
        this.productname = productname;
        this.producttype = producttype;
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
