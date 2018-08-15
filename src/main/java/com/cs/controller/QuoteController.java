package com.cs.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.domain.Company;
import com.cs.domain.Quote;
import com.cs.exception.InvalidParameterException;
import com.cs.service.CompanyService;
import com.cs.service.QuoteService;

@RestController
public class QuoteController {
	
	@Autowired
	QuoteService quoteSvc;
	
	@Autowired
	CompanyService companySvc;
	
	@RequestMapping("/quotes")
	public List<Quote> findQuotesWithFilterAndSortingCriteria(
				@RequestParam(value="tickerSymbol", defaultValue="")String tickerSymbol,
				@RequestParam(value="fromTimestamp", defaultValue="")String fromTimestamp,
				@RequestParam(value="toTimestamp", defaultValue="")String toTimestamp,
				@RequestParam(value="sortSeq", defaultValue="asc")String sortSequence) {
		
		if(!tickerSymbol.isEmpty()) {
			try {
				Company toFind = companySvc.findCompanyByTickerSymbol(tickerSymbol);
			} catch (EmptyResultDataAccessException e) {
				throw new InvalidParameterException("Ticker symbol does not exist.");
			}
		}
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime from = null;
		if(!fromTimestamp.isEmpty()) {
			try {
				from = formatter.parseDateTime(fromTimestamp);
			} catch(IllegalArgumentException e) {
				throw new InvalidParameterException("Invalid from timestamp. Input timestamp in yyyy-MM-dd HH:mm:ss format only.");
			}
		}
		
		DateTime to = null;
		if(!toTimestamp.isEmpty()) {
			try {
				to = formatter.parseDateTime(toTimestamp);
			} catch(IllegalArgumentException e) {
				throw new InvalidParameterException("Invalid to timestamp. Input timestamp in yyyy-MM-dd HH:mm:ss format only.");
			}
		}
		
		if(to != null && from != null) {
			if(to.isBefore(from)) {
				throw new InvalidParameterException("To timestamp cannot be before From timestamp.");
			}
		}else if (to != null || from != null) {
			throw new InvalidParameterException("Invalid timestamp parameters. Both from and to timestamps must be provided.");
		}
		
		if(!sortSequence.equals("asc") && !sortSequence.equals("desc")) {
			throw new InvalidParameterException("Sorting sequence only allows asc and desc.");
		}
		
		return quoteSvc.filterAndSortQuotesByCriteria(tickerSymbol, fromTimestamp, toTimestamp, sortSequence);
	}
	
}
