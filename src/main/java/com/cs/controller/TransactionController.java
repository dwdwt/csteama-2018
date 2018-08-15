package com.cs.controller;

import com.cs.domain.Order;
import com.cs.exception.InvalidParameterException;
import com.cs.service.OrderService;
import com.cs.service.TransactionService;
import com.cs.view.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<TransactionView>> findOrdersWithFilterAndSortingCriteria(
			@RequestParam(value="userId",required=false)String userId,
			@RequestParam(value="stockSymbol",required=false)String stockSymbol,
			@RequestParam(value="fromDate",required=false)String fromDate,
			@RequestParam(value="toDate",required=false)String toDate,
            @RequestParam(value="filter",required=false)String filterItemsString) {
            return new ResponseEntity<>(transactionService.getAllTransactionViewsByCriteria(userId, stockSymbol, fromDate, toDate, filterItemsString),HttpStatus.OK) ;

	}
	
}
