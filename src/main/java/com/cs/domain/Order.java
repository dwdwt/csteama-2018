package com.cs.domain;

import org.joda.time.DateTime;

public class Order {
    private int orderId;
    private Company company;
    private String side;
    private String type;
    private double price;
    private int noOfShares;
    private DateTime timeStamp;
    private User trader;
    private String status;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(int noOfShares) {
        this.noOfShares = noOfShares;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public User getTrader() {
        return trader;
    }

    public void setTrader(User trader) {
        this.trader = trader;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order(int orderId, Company company, String side, String type, double price, int noOfShares,
                 DateTime timeStamp, User trader, String status) {
        super();
        this.orderId = orderId;
        this.company = company;
        this.side = side;
        this.type = type;
        this.price = price;
        this.noOfShares = noOfShares;
        this.timeStamp = timeStamp;
        this.trader = trader;
        this.status = status;
    }
    
    public Order(Company company, String side, String type, double price, int noOfShares,
            DateTime timeStamp, User trader) {
	   super();
	   
	   this.company = company;
	   this.side = side;
	   this.type = type;
	   this.price = price;
	   this.noOfShares = noOfShares;
	   this.timeStamp = timeStamp;
	   this.trader = trader;
	   this.status = "OPENED";
	}

    public Order() {
        super();
    }

}
