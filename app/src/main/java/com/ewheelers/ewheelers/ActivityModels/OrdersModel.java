package com.ewheelers.ewheelers.ActivityModels;

public class OrdersModel {
    private String orderid;
    private String invoiceno;
    private String productname;
    private String vehtype;
    private String amount;
    private String date;
    private String status;

    public OrdersModel(String orderid, String invoiceno, String productname, String vehtype, String amount, String date, String status) {
        this.orderid = orderid;
        this.invoiceno = invoiceno;
        this.productname = productname;
        this.vehtype = vehtype;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public OrdersModel(String productname) {
        this.productname=productname;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }


    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getVehtype() {
        return vehtype;
    }

    public void setVehtype(String vehtype) {
        this.vehtype = vehtype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
