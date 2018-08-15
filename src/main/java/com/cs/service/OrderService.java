package com.cs.service;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
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
	
}
