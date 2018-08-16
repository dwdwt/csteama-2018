package com.cs.requst;

import com.cs.domain.Order;

public class OrderRequest {
    public String tickerSymbol;
    public String side;
    public String type;
    public Double price;
    public int noOfShares;
    public int traderId;

    public OrderRequest(){

    }

    public OrderRequest(String tickerSymbol, String side, String type, Double price, int noOfShares, int traderId) {
        this.tickerSymbol = tickerSymbol;
        this.side = side;
        this.type = type;
        this.price = price;
        this.noOfShares = noOfShares;
        this.traderId = traderId;
    }
}
