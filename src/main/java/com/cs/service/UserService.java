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
	
	
	public User getUser(int id){
		return userRepo.findUserById(id);
	}
	


}