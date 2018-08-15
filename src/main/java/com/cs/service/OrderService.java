package com.cs.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dao.OrderRepository;
import com.cs.dao.UserRepository;
import com.cs.domain.Order;
import com.cs.domain.User;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	UserRepository userRepo;
	
	public List<Order> findAllOrders(){
        return orderRepo.findAllOrders();
    }
	
	public Order findOrderById(int id){
        return orderRepo.findOrderById(id);
    }
	
	public List<Order> filterAndSortOrdersByCriteria(Map<String, String> criteriaMap, String sortParams, String sortSequence){
		return orderRepo.filterAndSortOrdersByCriteria(criteriaMap, sortParams, sortSequence);
	}
	
	public void cancelOrder(int id) {
		orderRepo.cancelOrder(id);
	}
	
	public void updateOrder(int id, Map<String, Object> updateMap) {
		orderRepo.updateOrder(id, updateMap);;
	}

	public Order insertOrder(Order order) {
		return orderRepo.insertOrder(order);
	}

	//find orders by user Id
	public List<Order> getOrdersByUserId(int uid){
		return orderRepo.findOrdersByUserId(uid);
	}
	
	//find last time order by userId
	public DateTime getLastOrderTimestamp(int uid) {
		return orderRepo.findLastOrderTimestamp(uid);
	}
	
	//find total order by status
	public int getTotalOrdersBystatus(int uid, String status) {
		return orderRepo.getOrderCountByStatus(uid, status);
	}
	
	//get top 5 by number of trades
	public List<User> getTop5tradersbyNumberofTrades(){
		List<Integer> list =  orderRepo.getTopfiveByNumberofTrades();
		List<User> a = new ArrayList();
		for (Integer e: list) {
			a.add(userRepo.findUserById(e));
		}
		return a;
    }
	
	
	

}
