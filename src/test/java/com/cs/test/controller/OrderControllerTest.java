package com.cs.test.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.domain.Order;
import com.cs.exception.InvalidActionException;
import com.cs.exception.InvalidParameterException;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.List;

import static io.restassured.RestAssured.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT,
				classes= {Csteama2018Application.class})
public class OrderControllerTest {
	@LocalServerPort
	private int serverPort;
	
	@Before
	public void init() {
		RestAssured.port=serverPort;
	}
	
	//Story 2 Tests
	@Test
	public void cancelOpenOrder() {
    	Response response =
		when().
    		get("/cancel/{orderId}",1).
    	then().
    		statusCode(200).
    	and().extract().response();
    	List<String> jsonResponse = response.jsonPath().getList("status");
    	String cancelledOrder = jsonResponse.get(jsonResponse.size()-1);
    	assertThat(cancelledOrder, is("CANCELLED"));
	 }
	
	@Test
	public void cancelFilledOrder() {
    	try {
    		when().
    			get("/cancel/{orderId}",2);
    	}catch(InvalidActionException e) {
    		assertThat(e.getMessage(), is("Order 2 has been filled or cancelled. Unable to cancel order."));
    	}
    }
	 
	@Test
	public void cancelInvalidOrder() {
		try {
			when().
				get("/cancel/{orderId}",100);
		}catch(InvalidParameterException e) {
			assertThat(e.getMessage(), is("Invalid order id."));
		}
		
	}
	
	@Test
	public void cancelCancelledOrder() {
		try {
			when().
				get("/cancel/{orderId}",3);
		}catch(InvalidActionException e) {
			assertThat(e.getMessage(), is("Order 3 has been filled or cancelled. Unable to cancel order."));
		}
		
	}
	
	
	
	//Story 4 Tests
	@Test
    public void allOrdersReturned() {
    	Response response =
		when()
			.get("/orders?side=B").
		then()
			.statusCode(200).
		and().extract().response();
    	List<String> jsonResponse = response.jsonPath().getList("orderId");
    	assertThat(jsonResponse.size(),is(4));
    }
}
