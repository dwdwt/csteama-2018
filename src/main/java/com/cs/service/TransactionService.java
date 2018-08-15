package com.cs.service;

import com.cs.dao.OrderRepository;
import com.cs.dao.TransactionRepository;
import com.cs.domain.Operation;
import com.cs.domain.Order;
import com.cs.domain.Transaction;
import com.cs.view.TransactionView;
import org.assertj.core.util.Strings;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	OrderRepository orderRepository;

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

	public List<TransactionView> getAllTransactionViewsByCriteria(String userId, String stockSymbol, String fromDate, String toDate, String returnItemString) {

		List<TransactionView> transactionViewsList = new ArrayList<TransactionView>();
		transactionRepository.findTransactionByCriteria(userId,stockSymbol,fromDate,toDate)
				.stream()
				.forEach(it -> transactionViewsList.add(Strings.isNullOrEmpty(returnItemString) ?
						new TransactionView(it,orderRepository.findOrderById(it.getOrderId())) :
						new TransactionView(it,orderRepository.findOrderById(it.getOrderId()),returnItemString.split(","))));
		return transactionViewsList;
	}
}
