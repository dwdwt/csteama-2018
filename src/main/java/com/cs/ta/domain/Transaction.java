package com.cs.ta.domain;

import org.joda.time.DateTime;

public class Transaction {

    private int id;
    private int orderId;
    private Operation operation;
    private double price;
    private int quantity;
    private DateTime txnTimeStamp;

    public Transaction() {
    }

    public Transaction(int id, int orderId, Operation operation, double price, int quantity, DateTime txnTimeStamp) {
        this.id = id;
        this.orderId = orderId;
        this.operation = operation;
        this.price = price;
        this.quantity = quantity;
        this.txnTimeStamp = txnTimeStamp;
    }

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

    public DateTime getTxnTimeStamp() {
        return txnTimeStamp;
    }

    public void setTxnTimeStamp(DateTime txnTimeStam) {
        this.txnTimeStamp = txnTimeStam;
    }




}
