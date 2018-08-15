package com.cs.service;

import java.util.List;

import com.cs.dao.OrderRepository;
import com.cs.exception.InvalidActionException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dao.UserRepository;
import com.cs.domain.Operation;
import com.cs.domain.Order;
import com.cs.domain.Role;
import com.cs.domain.Transaction;
import com.cs.domain.User;

@Service
public class UserService {
	@Autowired
	UserRepository userRepo;

	@Autowired
    OrderRepository orderRepository;

	public List<User> findAllTraders(){
        return userRepo.findAllUsers();
	}
	
	
	public User getUserById(int id){
		return userRepo.findUserById(id);
	}

	public List<User> getAllUsers(){
		return userRepo.findAllUsers();
	}

	public void addUser(User user) {
		userRepo.insertUser(user);
		return;
	}

	public void removeUser(int id) {
		checkIfAnyOrderStatusForUser(id);
		userRepo.deleteUserById(id);
		return;
	}

	private void checkIfAnyOrderStatusForUser(int id) {
		if (orderRepository.userHasAnyOrder(id)) throw new InvalidActionException("This trader has orders in any status");
	}

}