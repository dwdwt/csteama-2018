package com.cs.dao;

import com.cs.ta.domain.Operation;
import com.cs.ta.domain.Order;
import com.cs.ta.domain.Transaction;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Order> findAll(){
        return jdbcTemplate.query("select * from orders", new OrderRowMapper());
    }

    class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setOrderId(rs.getInt("id"));
            //@TODO: Map Trader and Companay Object here
            order.setTrader(null/*rs.getString("userId")*/);
            order.setCompany(null/*rs.getString("tickerSymbol")*/);
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
