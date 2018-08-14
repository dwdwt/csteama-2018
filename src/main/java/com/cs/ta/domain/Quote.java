package com.cs.ta.domain;

import org.joda.time.DateTime;

public class Quote {
	private Order buyOrder;
	private Order sellOrder;
	private int noOfShares;
	private DateTime timestamp;
	public Order getBuyOrder() {
		return buyOrder;
	}
	public void setBuyOrder(Order buyOrder) {
		this.buyOrder = buyOrder;
	}
	public Order getSellOrder() {
		return sellOrder;
	}
	public void setSellOrder(Order sellOrder) {
		this.sellOrder = sellOrder;
	}
	public int getNoOfShares() {
		return noOfShares;
	}
	public void setNoOfShares(int noOfShares) {
		this.noOfShares = noOfShares;
	}
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
	public Quote(Order buyOrder, Order sellOrder, int noOfShares, DateTime timestamp) {
		super();
		this.buyOrder = buyOrder;
		this.sellOrder = sellOrder;
		this.noOfShares = noOfShares;
		this.timestamp = timestamp;
	}
	public Quote() {
		super();
	}
	
}
