package com.cs.service;

import com.cs.dao.OrderRepository;
import com.cs.dao.TransactionRepository;
import com.cs.domain.Operation;
import com.cs.domain.Order;
import com.cs.domain.Transaction;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	public List<Transaction> getAllTransactions(){
		return transactionRepository.findAll();
	}

	public Transaction getTransaction(int id){
		return transactionRepository.findById(id);
	}

	public void removeTransaction(int id){
		transactionRepository.removeTxn(id);
	}

	public void addTxnRecord(Order order, Operation operation, double price, int quantity, DateTime txnDateTime){
		transactionRepository.insertTxn(new Transaction(order.getOrderId(),operation,price,quantity, txnDateTime));
		return;
	}


}
