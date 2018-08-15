package com.cs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.domain.Order;
import com.cs.exception.InvalidActionException;
import com.cs.exception.InvalidParameterException;
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
	
	@RequestMapping("/cancel/{orderId}")
	public List<Order> cancelOrder(@PathVariable("orderId")int orderId) {
		try {
			Order order = orderSvc.findOrderById(orderId);
			
			if(!order.getStatus().matches("FILLED|CANCELLED")) {
				orderSvc.cancelOrder(orderId);
			} else {
				throw new InvalidActionException("Order " + orderId + " has been filled or cancelled. Unable to cancel order.");
			}
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidParameterException("Invalid order id.");
		}
		return orderSvc.findAllOrders();
	}
	
	@RequestMapping("/update/{orderId}")
	public List<Order> updateOrder(@PathVariable("orderId")int orderId,
			@RequestParam(value="quantity", defaultValue="")String noOfShares,
			@RequestParam(value="price", defaultValue="")String sharePrice,
			@RequestParam(value="orderType", defaultValue="")String orderType) {
		
		if(noOfShares.isEmpty() && sharePrice.isEmpty() && orderType.isEmpty()) {
			throw new InvalidParameterException("No update parameters found.");
		}
		
		Integer shares = null;
		if(!noOfShares.isEmpty()) {
			try {
				shares = Integer.parseInt(noOfShares);
			} catch (NumberFormatException e) {
				throw new InvalidParameterException("Number of shares has to be an integer value.");
			}
		}
		
		Double price = null;
		if(!sharePrice.isEmpty()) {
			try {
				price = Double.parseDouble(sharePrice);
			} catch (NumberFormatException e) {
				throw new InvalidParameterException("Price has to be a double value.");
			}
		}
		
		if(!orderType.isEmpty() && !orderType.matches("MARKET|LIMIT")) {
			throw new InvalidParameterException("Order type has to be either MARKET or LIMIT.");
		}
		
		Map<String, Object> updateMap = new HashMap<>();
		if(!noOfShares.isEmpty())updateMap.put("noOfShares",shares);
		if(!sharePrice.isEmpty())updateMap.put("price",price);
		if(!orderType.isEmpty())updateMap.put("orderType",orderType);
		
		try {
			Order order = orderSvc.findOrderById(orderId);
			
			if(order.getStatus().matches("FILLED|CANCELLED")) {
				throw new InvalidActionException("Order " + orderId + " has been filled/cancelled. Unable to update order.");
			} 
			
			orderSvc.updateOrder(orderId, updateMap);
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidParameterException("Invalid order id.");
		}
		return orderSvc.findAllOrders();
	}
	
}
