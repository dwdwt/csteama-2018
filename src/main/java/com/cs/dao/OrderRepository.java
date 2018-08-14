package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cs.domain.Order;


@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private CompanyRepository companyRepo;

    public List<Order> findAllOrders(){
        return jdbcTemplate.query("SELECT * from ORDERS GROUP BY side, orderType, status", new OrderRowMapper());
    }
    
    public List<Order> filterAndSortOrdersByCriteria(Map<String, String> criteriaMap, String sortParams, String sortSequence){

		String query = "SELECT * from ORDERS";
		
		if (!criteriaMap.isEmpty()) {
			query += " WHERE ";
			
			// Filter Qty
        	try {
        		int fromOrderQty = Integer.parseInt(criteriaMap.get("fromOrderQty"));
        		int toOrderQty = Integer.parseInt(criteriaMap.get("toOrderQty"));
        		query += ("(noOfShares BETWEEN " + fromOrderQty + " AND " + toOrderQty + ") AND ");
        	} catch (NumberFormatException e) {}
        	
        	// Filter timestamp
    		String fromTimestamp = criteriaMap.get("fromTimestamp");
    		String toTimestamp = criteriaMap.get("toTimestamp");
    		if(fromTimestamp != null && toTimestamp != null) {
    			query += ("(orderTimeStamp BETWEEN '" + fromTimestamp + "' AND '" + toTimestamp + "') AND ");
    		}
        	
    		criteriaMap.remove("fromOrderQty");
    		criteriaMap.remove("toOrderQty");
    		criteriaMap.remove("fromTimestamp");
    		criteriaMap.remove("toTimestamp");
        	
        	Iterator criteriaIterator = criteriaMap.entrySet().iterator();
    		while (criteriaIterator.hasNext()) {
    		    Map.Entry pair = (Map.Entry)criteriaIterator.next();
    		    String key = (String) pair.getKey();
    		    String value = (String) pair.getValue();
    	    	query += (key + "='" + value + "' AND "); 
    		}
    		// Remove the last AND
    		query = query.substring(0, query.length()-5);
		}
		
    	query += " GROUP BY side, orderType, status";
    	 
    	if (!sortParams.isEmpty()) {
    		query += " ORDER BY " + sortParams + " " + sortSequence;
    	}
		
		return jdbcTemplate.query(query, new OrderRowMapper());
    	
    }
     
    class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setOrderId(rs.getInt("id"));
            order.setTrader(userRepo.findUserById(rs.getInt("id")));
            order.setCompany(companyRepo.findCompanyByTickerSymbol(rs.getString("tickerSymbol")));
            order.setSide(rs.getString("side"));
            order.setType(rs.getString("orderType"));
            order.setPrice(rs.getDouble("price"));
            order.setNoOfShares(rs.getInt("noOfShares"));
            order.setTimeStamp(new DateTime(rs.getTimestamp("orderTimeStamp")));
            order.setStatus(rs.getString("status"));
            return order;
        }
    }
}
