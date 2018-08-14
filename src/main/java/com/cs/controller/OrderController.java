package com.cs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.domain.Order;
import com.cs.service.OrderService;

@RestController
public class OrderController {
	
	@Autowired
	OrderService orderSvc;
	
	@RequestMapping(value="/orders")
	public List<Order> findAllOrders() {
		return orderSvc.findAllOrders();
	}
	
//	@RequestMapping(value="/users/{userId}")
//	public User getUserById(@PathVariable("userId") int id) {
//		return userSvc.findUserById(id);
//	}
	
}
