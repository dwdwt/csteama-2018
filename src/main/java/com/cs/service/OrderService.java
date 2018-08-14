package com.cs.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dao.OrderRepository;
import com.cs.domain.Order;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepo;
	
	public List<Order> findAllOrders(){
        return orderRepo.findAllOrders();
    }
	
	public List<Order> filterOrdersByCriteria(Map<String, String> criteriaMap){
		return orderRepo.filterOrdersByCriteria(criteriaMap);
	}
}