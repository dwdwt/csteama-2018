package com.cs.dao;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cs.domain.Operation;
import com.cs.domain.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Transaction> findAll(){
        return jdbcTemplate.query("select * from transactions", new TransactionRowMapper());
    }

    public Transaction findById(int id) {
        return jdbcTemplate.queryForObject("select * from transactions where id =?", new TransactionRowMapper(), id);
    }

    //@TODO: UNFINISHED
    public void insertTxn(Transaction txn) {
//        "INSERT INTO transactions()"
////        return jdbcTemplate.create
    }

    public void update(String sql){
        jdbcTemplate.update(sql);
    }

    class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction txn = new Transaction();
            txn.setId(rs.getInt("id"));
            txn.setOrderId(rs.getInt("orderId"));
            txn.setPrice(rs.getDouble("price"));
            txn.setOperation(Operation.valueOf(rs.getString("operation")));
            txn.setQuantity(rs.getInt("quantity"));
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
            txn.setTxnTimeStamp(formatter.parseDateTime(rs.getString("txnTimeStamp")));

            return txn;
        }
    }
}
