package com.cs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.domain.Order;
import com.cs.service.OrderService;

@RestController
public class OrderController {
	
	@Autowired
	OrderService orderSvc;
	
//	@RequestMapping(value="/orders")
//	public List<Order> findAllOrders() {
//		return orderSvc.findAllOrders();
//	}
	
	@RequestMapping("/orders")
	public List<Order> findOrdersWithFilterAndSortingCriteria(
				@RequestParam(value="side", defaultValue="")String side,
				@RequestParam(value="orderType", defaultValue="")String type,
				@RequestParam(value="status", defaultValue="")String status,
				@RequestParam(value="tickerSymbol", defaultValue="")String tickerSymbol,
				@RequestParam(value="fromOrderQty", defaultValue="")String fromOrderQty,
				@RequestParam(value="toOrderQty", defaultValue="")String toOrderQty,
				@RequestParam(value="fromTimestamp", defaultValue="")String fromTimestamp,
				@RequestParam(value="toTimestamp", defaultValue="")String toTimestamp,
				@RequestParam(value="sort", defaultValue="")String sortParams,
				@RequestParam(value="sortSeq", defaultValue="")String sortSequence) {
		
		Map<String, String> criteriaMap = new HashMap<>();
		
		if(!side.isEmpty())criteriaMap.put("side",side);
		if(!type.isEmpty())criteriaMap.put("orderType",type);
		if(!status.isEmpty())criteriaMap.put("status",status);
		if(!tickerSymbol.isEmpty())criteriaMap.put("tickerSymbol",tickerSymbol);
		if(!fromOrderQty.isEmpty())criteriaMap.put("fromOrderQty",fromOrderQty);
		if(!toOrderQty.isEmpty())criteriaMap.put("toOrderQty",toOrderQty);
		if(!fromTimestamp.isEmpty())criteriaMap.put("fromTimestamp",fromTimestamp);
		if(!toTimestamp.isEmpty())criteriaMap.put("toTimestamp",toTimestamp);
		
		return orderSvc.filterAndSortOrdersByCriteria(criteriaMap, sortParams, sortSequence);
	}
	
//	@RequestMapping(value="/users/{userId}")
//	public User getUserById(@PathVariable("userId") int id) {
//		return userSvc.findUserById(id);
//	}
	
}
