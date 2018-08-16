package com.cs.dao;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cs.domain.Operation;
import com.cs.domain.Order;
import com.cs.domain.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

@Repository
public class TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Transaction> findAll() {
        return jdbcTemplate.query("select * from transactions", new TransactionRowMapper());
    }

    public Transaction findById(int id) {
        return jdbcTemplate.queryForObject("select * from transactions where id =?", new TransactionRowMapper(), id);
    }


    public void insertTxn(Transaction txn) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String sql = MessageFormat.format("INSERT INTO transactions(orderId,operation,price,quantity,txnTimeStamp) values ({0},{1},{2},{3},{4})",
                txn.getOrderId(),
                "'" + txn.getOperation() + "'",
                txn.getPrice(),
                txn.getQuantity(),
                "'" +  dtfOut.print(txn.getTxnTimeStamp()) + "'");
        update(sql);
    }

    public void removeTxn(int id) {
        String sql = MessageFormat.format("DELETE from transactions where id = {0}", id);
        update(sql);
    }

    public List<Transaction> findTransactionByCriteria(String userid, String stockSymbol, String fromDate, String toDate){
        QueryBuilder queryBuilder = new QueryBuilder();
        String query = queryBuilder.createCriteria().setUserId(userid).setSymbol(stockSymbol).setFrom(fromDate).setTo(toDate).inDescByTimeStamp().toString();
        return jdbcTemplate.query(query, new TransactionRowMapper());
    }


    public void update(String sql) {
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
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            txn.setTxnTimeStamp(formatter.parseDateTime(rs.getString("txnTimeStamp")));

            return txn;
        }
    }

    static class QueryBuilder {

        String query = "";
        public QueryBuilder createCriteria(){
            query += "select * from transactions t join orders o on t.orderid = o.id where 1=1 ";
            return this;

        }

        public QueryBuilder setUserId(String userId){
            if (userId != null)   query += " and o.userId = " + userId;
            return this;
        }

        public QueryBuilder setSymbol(String symbol){
            if (symbol != null) query += " and o.tickerSymbol = '" + symbol + "'";
            return this;

        }

        public QueryBuilder setFrom(String fromDate){
            if (fromDate != null) query += " and t.txnTimeStamp >= '" + fromDate + "'";
            return this;

        }

        public QueryBuilder setTo(String toDate){
            if (toDate != null) query += " and t.txnTimeStamp <= '" + toDate + "'";
            return this;
        }

        public QueryBuilder inDescByTimeStamp(){ query += " order by t.txnTimeStamp desc ";
            return this;
        }

        public String toString(){
            return query;

        }
    }
   
}
