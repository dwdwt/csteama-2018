package com.cs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dao.QuoteRepository;
import com.cs.domain.Quote;
import com.cs.domain.User;

@Service
public class QuoteService {
	
	@Autowired
	QuoteRepository quoteRepo;
	
	private static boolean ASC = true;
	private static boolean DESC = false;
	
	public List<Quote> findAllQuotes() {
		return quoteRepo.findAllQuotes();
	}
	
	public List<Quote> filterAndSortQuotesByCriteria(String tickerSymbol, String fromTimestamp, String toTimestamp, String sortSequence){
		return quoteRepo.filterAndSortQuotesByCriteria(tickerSymbol, fromTimestamp, toTimestamp, sortSequence);
	}
	
	
	public List<User> findtop5byVolume(){

		List<User> a = new ArrayList();
		

		
		List<Quote> lista = quoteRepo.findAllQuotes();
		HashMap<User, Double> listallvolumea = new HashMap<>();
		
		
		for (Quote qt: lista) {
			if(listallvolumea.containsKey(qt.getBuyOrder().getTrader())) {
				double addup = qt.getNoOfShares()*qt.getBuyOrder().getPrice();
				listallvolumea.put(qt.getBuyOrder().getTrader(), addup);
			}
			else if(listallvolumea.containsKey(qt.getSellOrder().getTrader())) {
				double addup = qt.getNoOfShares()*qt.getSellOrder().getPrice();
				listallvolumea.put(qt.getSellOrder().getTrader(), addup);
			}
			else {
				listallvolumea.put(qt.getBuyOrder().getTrader(), qt.getNoOfShares()*qt.getBuyOrder().getPrice());
				listallvolumea.put(qt.getSellOrder().getTrader(), qt.getNoOfShares()*qt.getSellOrder().getPrice());
			}
		}
		
		//listallvolumea.entrySet()
		
		
		return null;
		

		
		
		
	}
	

}
