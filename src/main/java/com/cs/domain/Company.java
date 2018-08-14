package com.cs.domain;

public class Company {
	private String tickerSymbol;
	private String name;
	private Industry industry;
	public String getTickerSymbol() {
		return tickerSymbol;
	}
	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Industry getIndustry() {
		return industry;
	}
	public void setIndustry(Industry industry) {
		this.industry = industry;
	}
	public Company(String tickerSymbol, String name, Industry industry) {
		super();
		this.tickerSymbol = tickerSymbol;
		this.name = name;
		this.industry = industry;
	}
	public Company() {
		super();
	}
	
}
