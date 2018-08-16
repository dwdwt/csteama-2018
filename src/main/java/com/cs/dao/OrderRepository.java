package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cs.domain.Order;


@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private CompanyRepository companyRepo;

    @Transactional
    public List<Order> findAllOrders(){
        return jdbcTemplate.query("SELECT * FROM orders", new OrderRowMapper());
    }

    @Transactional
    public Order findOrderById(int id) {
    	return jdbcTemplate.queryForObject("SELECT * FROM orders WHERE id = ?", new OrderRowMapper(), id);
    }
    
    public double findOrderPriceById(int id) {
    	String query = "SELECT price FROM orders WHERE id = ?";
    	return jdbcTemplate.queryForObject(query, new Object[] { id }, Double.class);
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

	public List<Order> getAllOppositeOrders(Order order){
		return jdbcTemplate.query(
				MessageFormat.format("SELECT * FROM orders WHERE tickerSymbol = {0} AND side <> {1} order by price",
						"'" + order.getCompany().getTickerSymbol() + "'",
						"'" + order.getSide() + "'")
				, new OrderRowMapper());
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
    @Transactional
    public Order insertOrder(Order order) {
    	String query = "INSERT into ORDERS (userid,tickersymbol,side,ordertype,price,noofshares,status,ordertimestamp) values("
    			+ order.getTrader().getId()+ ",'"
    			+ order.getCompany().getTickerSymbol() + "','"
    			+ order.getSide() + "','" + order.getType() + "',"
    			+ order.getPrice() + "," + order.getNoOfShares() + ",'"
    			+ order.getStatus() + "','" + order.getTimeStamp() +"')";
    	jdbcTemplate.execute(query);
    	List<Order> orderList = findAllOrders();
    	Order insertedOrder = orderList.get(orderList.size()-1);
    	return insertedOrder;
    }

    @Transactional
	public Order updateOrder(Order order) {
		String query = MessageFormat.format("Update ORDERS set userid = {0}, tickersymbol = {1},side = {2},ordertype = {3},price = {4},noofshares = {5},status = {6}, ordertimestamp = {7} where id = {8}",
				order.getTrader().getId(),
				 "'" + order.getCompany().getTickerSymbol() + "'",
				"'" + order.getSide() + "'" ,
				"'" + order.getType()+"'",
				 order.getPrice(),order.getNoOfShares(),
				"'"+ order.getStatus() + "'",
				"'" + order.getTimeStamp()+"'",
				order.getOrderId());
		jdbcTemplate.execute(query);
		return findOrderById(order.getOrderId());

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
    
    
    //top 5 traders by number of tardes
    public List<Integer> getTopfiveByNumberofTrades(){
    	String query = "SELECT userId FROM orders WHERE status = 'FILLED' GROUP BY userId ORDER BY COUNT(id) DESC LIMIT 5 ";
    	List<Integer> top5 = jdbcTemplate.queryForList(query, Integer.class);
    	return top5;
    
    	//return null;
    	
    }

    //for rollback after insert - junit
  	public void deleteOrder(int orderId) {
  		String query ="DELETE FROM orders WHERE id = " + orderId;
  		jdbcTemplate.update(query);
  	}



}
