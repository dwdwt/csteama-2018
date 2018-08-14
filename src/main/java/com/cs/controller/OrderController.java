package com.cs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cs.service.OrderService;

@RestController
public class OrderController {
	
	@Autowired
	OrderService orderSvc;
	
//	@RequestMapping(value="/orders")
//	public List<Order> getAllOrders() {
//		return userSvc.findAllUsers();
//	}
	
//	@RequestMapping(value="/users/{userId}")
//	public User getUserById(@PathVariable("userId") int id) {
//		return userSvc.findUserById(id);
//	}
	
}
