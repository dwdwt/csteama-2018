package com.cs.service;

import java.util.List;

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
	
	public List<User> findAllTraders(){
        return userRepo.findAllUsers();
	}
	
	
	public User getUserById(int id){
		return userRepo.findUserById(id);
	}

	public List<User> getAllUsers(){
		return userRepo.findAllUsers();
	}
	
	
	public void addUsers(int uid, String firstName, String lastName, String contact, String email, String address) {
		User ur = new User(uid, firstName, lastName, contact, email, Role.TRADER, address);
		userRepo.insertUser(ur);
		return;
	}

	public void addUser(User user) {
		userRepo.insertUser(user);
		return;
	}

	public void removeUser(int id) {
		userRepo.deleteUserById(id);
		return;
	}

}