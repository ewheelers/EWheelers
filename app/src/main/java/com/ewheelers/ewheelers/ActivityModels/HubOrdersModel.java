package com.ewheelers.ewheelers.ActivityModels;

public class HubOrdersModel {
    private String completedOrders;
    private String completedAmount;
    private String inprocessOrders;
    private String inprocessAmount;
    private String pendingOrders;
    private String pendingAmount;
    private String totalOrders;
    private String totalAmount;



    public HubOrdersModel(String completedOrders, String completedAmount, String inprocessOrders, String inprocessAmount, String pendingOrders, String pendingAmount, String totalOrders, String totalAmount) {
        this.completedOrders = completedOrders;
        this.completedAmount = completedAmount;
        this.inprocessOrders = inprocessOrders;
        this.inprocessAmount = inprocessAmount;
        this.pendingOrders = pendingOrders;
        this.pendingAmount = pendingAmount;
        this.totalOrders = totalOrders;
        this.totalAmount = totalAmount;
    }

    public String getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(String completedOrders) {
        this.completedOrders = completedOrders;
    }

    public String getCompletedAmount() {
        return completedAmount;
    }

    public void setCompletedAmount(String completedAmount) {
        this.completedAmount = completedAmount;
    }

    public String getInprocessOrders() {
        return inprocessOrders;
    }

    public void setInprocessOrders(String inprocessOrders) {
        this.inprocessOrders = inprocessOrders;
    }

    public String getInprocessAmount() {
        return inprocessAmount;
    }

    public void setInprocessAmount(String inprocessAmount) {
        this.inprocessAmount = inprocessAmount;
    }

    public String getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(String pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
