package com.cs.domain;

import org.joda.time.DateTime;

public class Transaction {

    private int id;
    private int orderId;
    private Operation operation;
    private double price;
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DateTime getTxnTimeStam() {
        return txnTimeStam;
    }

    public void setTxnTimeStamp(DateTime txnTimeStam) {
        this.txnTimeStam = txnTimeStam;
    }

    private DateTime txnTimeStam;


}
