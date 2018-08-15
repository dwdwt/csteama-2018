package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cs.dao.UserRepository.UserRowMapper;
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
        return jdbcTemplate.query("SELECT * FROM orders", new OrderRowMapper());
    }

    public Order findOrderById(int id) {
    	return jdbcTemplate.queryForObject("SELECT * FROM orders WHERE id = ?", new OrderRowMapper(), id);
    }
    
    public List<Order> filterAndSortOrdersByCriteria(Map<String, String> criteriaMap, String sortParams, String sortSequence){

		String query = "SELECT * FROM orders";
		
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

    	if (!sortParams.isEmpty()) {
    		query += " ORDER BY " + sortParams + " " + sortSequence;
    	}
		
		return jdbcTemplate.query(query, new OrderRowMapper());
    	
    }

    public boolean userHasAnyOrder(int userId){
		List<Order> orders =  jdbcTemplate.query("SELECT * FROM orders WHERE userid = ?", new OrderRowMapper(), userId);
		return !orders.isEmpty();
	}

    public void cancelOrder(int id) {
    	jdbcTemplate.update("UPDATE orders SET status = 'CANCELLED' where id = ?", id);
    }
    
    public void updateOrder(int id, Map<String, Object> updateMap) {
    	String query = "UPDATE orders SET ";
    	
    	if (updateMap != null) {
    		Iterator updateIterator = updateMap.entrySet().iterator();
    		while (updateIterator.hasNext()) {
    		    Map.Entry pair = (Map.Entry)updateIterator.next();
    		    String key = (String) pair.getKey();
    		    Object value = pair.getValue();
    		    if (value != null && value instanceof String) {
    		    	query += (key + "='" + value + "',");
    		    } else {
    		    	query += (key + "=" + value + ",");
    		    }
    		}
    		
    		// Remove the last ,
    		query = query.substring(0, query.length()-1) + " WHERE id = ?";
    		jdbcTemplate.update(query, id);
    	}
    }

    public Order insertOrder(Order order) {
    	String query = "INSERT into ORDERS (userid,tickersymbol,side,ordertype,price,noofshares,status,ordertimestamp) values("
    			+ order.getTrader().getId()+ ",'"
    			+ order.getCompany().getTickerSymbol() + "','"
    			+ order.getSide() + "','" + order.getType() + "',"
    			+ order.getPrice() + "," + order.getNoOfShares() + ",'"
    			+ order.getStatus() + "','" + order.getTimeStamp() +"')";
    	System.out.println("****!!!!" + order.getTrader().getId());
    	jdbcTemplate.execute(query);
    	List<Order> orderList = findAllOrders();
    	Order insertedOrder = orderList.get(orderList.size()-1);
    	return insertedOrder;
    }
    class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setOrderId(rs.getInt("id"));
            order.setTrader(userRepo.findUserById(rs.getInt("userid")));
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


    //find order by userId
    public List<Order> findOrdersByUserId(int userId){
    	return jdbcTemplate.query("SELECT * FROM orders WHERE userId = ?", new OrderRowMapper(), userId);
    }
    
    //retrive last order by userId
    public DateTime findLastOrderTimestamp(int userId) {
    	String query = "SELECT MAX(orderTimeStamp) FROM orders WHERE userId = ?";
    	return new DateTime(jdbcTemplate.queryForObject(query, new Object[] { userId }, Timestamp.class));
    }
   
   //retrive total number or orders in system 
    public int getOrderCountByStatus(int userId, String status) {
    	String query = " SELECT Count(*) FROM orders WHERE ";
    	query += " userId = " + userId + " And status = '" + status + "'";
    	return jdbcTemplate.queryForObject(query, Integer.class);
    }
    
}
