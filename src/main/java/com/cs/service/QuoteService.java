package com.cs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dao.QuoteRepository;
import com.cs.domain.Quote;

@Service
public class QuoteService {
	
	@Autowired
	QuoteRepository quoteRepo;
	
	public List<Quote> findAllQuotes() {
		return quoteRepo.findAllQuotes();
	}
	
	public List<Quote> filterAndSortQuotesByCriteria(String tickerSymbol, String fromTimestamp, String toTimestamp, String sortSequence){
		return quoteRepo.filterAndSortQuotesByCriteria(tickerSymbol, fromTimestamp, toTimestamp, sortSequence);
	}
}
