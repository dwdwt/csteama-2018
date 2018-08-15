package com.cs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.cs.domain.Order;
import com.cs.domain.User;
import com.cs.service.OrderService;
import com.cs.service.UserService;
import com.cs.view.TraderOrderView;

import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {
	@Autowired
	UserService userSvc;
	
	@Autowired
	OrderService orderSvc;
	
	
	@RequestMapping("/users")
	public List<User> findAllUsers(){
		return userSvc.getAllUsers();
	}



	@RequestMapping(value = "/users", method = RequestMethod.POST,
			produces={MediaType.APPLICATION_JSON_VALUE},
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addUser(@RequestBody User user){
		userSvc.addUser(user);
	}


	@RequestMapping("/user/{id}")
	public User findUserById(
			@PathVariable int id) {
		return userSvc.getUserById(id);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public void removeUser(@PathVariable int id){
		userSvc.removeUser(id);
	}
	
    //top 5 traders by trade 
    @RequestMapping("/trader/top5")
    public List<User> findTopFiveTrader() {
        return orderSvc.getTop5tradersbyNumberofTrades();
    }



}
