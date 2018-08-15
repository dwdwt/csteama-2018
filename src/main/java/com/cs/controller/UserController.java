package com.cs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.domain.User;
import com.cs.service.UserService;
import com.cs.view.TransactionView;

@RestController
public class UserController {
	@Autowired
	UserService userSvc;
	
	
	@RequestMapping("/users")
	public  User  findAllUsers(
			@RequestParam(value="userId",required=false)int userId){
		return userSvc.getUser(userId);
	}
	

}
