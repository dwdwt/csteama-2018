package com.cs.controller;

import com.cs.domain.Order;
import com.cs.service.OrderService;
import com.cs.service.TransactionService;
import com.cs.view.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;

	
	@RequestMapping("/transactions")
	public List<TransactionView> findAllTransactionsView(){
		return transactionService.getAllTransactionViews();
	}
	
}
