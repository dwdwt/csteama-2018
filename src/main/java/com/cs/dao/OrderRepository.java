package com.cs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    
    public List<Order> filterOrdersByCriteria(Map<String, String> criteriaMap){
    	String query = "SELECT * from ORDERS GROUP BY side, type, status WHERE ";
    	Iterator criteriaIterator = criteriaMap.entrySet().iterator();
		while (criteriaIterator.hasNext()) {
		    Map.Entry pair = (Map.Entry)criteriaIterator.next();
		    String key = (String) pair.getKey();
		    String value = (String) pair.getValue();
		    try {
		    	int intValue = Integer.parseInt(value);
		    	query += (key + "=" + intValue + " AND ");
		    }catch(NumberFormatException e) {
		    	query += (key + "='" + value + "' AND "); 
		    }
		}
		// Remove the last AND
		query = query.substring(0, query.length()-4);
		
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
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
            order.setTimeStamp(formatter.parseDateTime(rs.getString("orderTimeStamp")));
            return order;
        }
    }
}
