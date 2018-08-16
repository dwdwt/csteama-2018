package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cs.domain.Quote;

@Repository
public class QuoteRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private OrderRepository orderRepo;
	
	public List<Quote> findAllQuotes() {
		return jdbcTemplate.query("SELECT * FROM quotes", new QuoteRowMapper());
	}
	
	public List<Quote> filterAndSortQuotesByCriteria(String tickerSymbol, String fromTimestamp, String toTimestamp, String sortSequence){
		String query = "SELECT * FROM quotes INNER JOIN orders ON quotes.buyOrderId=orders.id ";
		
		if(!tickerSymbol.isEmpty() || !fromTimestamp.isEmpty()) {
			query += "WHERE ";
			
			if(!tickerSymbol.isEmpty()) {
				query += "tickerSymbol='" + tickerSymbol + "' AND ";
			}
			
			if(!fromTimestamp.isEmpty() && !toTimestamp.isEmpty()) {
				query += ("(quoteTimeStamp BETWEEN '" + fromTimestamp + "' AND '" + toTimestamp + "') AND ");
			}
			
			// Remove the last AND
    		query = query.substring(0, query.length()-4);
		}
		
		
		
		query += "ORDER BY quoteTimeStamp " + sortSequence;
		
		return jdbcTemplate.query(query, new QuoteRowMapper());
	}
	
	
	
	class QuoteRowMapper implements RowMapper<Quote> {

		@Override
		public Quote mapRow(ResultSet rs, int rowNum) throws SQLException {
			Quote quote = null;
			quote = new Quote(orderRepo.findOrderById(rs.getInt("buyOrderId")), 
					orderRepo.findOrderById(rs.getInt("sellOrderId")), 
					rs.getInt("noOfShares"),
					new DateTime(rs.getTimestamp("quoteTimeStamp")));
			return quote;
		}
		
	}
	
	
    public  HashMap<Integer,Integer> topfiveQuotesFrombuy(){
    	String query = "SELECT sum(noOfShares) as 'quant', buyOrderId FROM quotes GROUP BY buyOrderId ORDER BY sum(noOfShares) DESC LIMIT 5";
    	//List<Map<int, int>> top5buy = jdbcTemplate.queryForMap(query, Integer.class);
    	//return top5buy;
    	
    	HashMap<Integer,Integer> results = new HashMap<>();
    	jdbcTemplate.query(query, (ResultSet rs) -> {
    	 
    	    while (rs.next()) {
    	        results.put(rs.getInt("buyOrderId"), rs.getInt("quant"));
    	    }
    	});
	    return results;
    }
    
    public HashMap<Integer,Integer> topfiveQuotesFromSell(){
    	String query = "SELECT sum(noOfShares) as 'quant', sellOrderId FROM quotes GROUP BY sellOrderId ORDER BY sum(noOfShares) DESC LIMIT 5";
    	//List<Integer> top5sell = jdbcTemplate.queryForList(query, Integer.class);
    	//return top5sell;
    	
    	HashMap<Integer,Integer> results = new HashMap<>();
    	jdbcTemplate.query(query, (ResultSet rs) -> {
    	 
    	    while (rs.next()) {
    	        results.put(rs.getInt("sellOrderId"), rs.getInt("quant"));
    	    }
    	});
	    return results;
    }
    

}
