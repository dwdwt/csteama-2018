package com.cs.test.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.cs.Csteama2018Application;
import com.cs.dao.OrderRepository;
import com.cs.domain.Company;
import com.cs.domain.Industry;
import com.cs.domain.Order;
import com.cs.domain.Role;
import com.cs.domain.User;
import com.cs.exception.InvalidActionException;
import com.cs.exception.InvalidParameterException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
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
	@Autowired
	public OrderRepository orderRepo;
	@Before
	public void init() {
		RestAssured.port=serverPort;
	}
	//Story 1 Tests
	@Test
	public void insertBuyMarketOrder() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		Industry industry = new Industry("Commodities Trading","Commodities Services");
    	Company company = new Company("XYZ.DE","CS", industry);
    	User user = new User(10,"Jane","Dong", "4321","janedong@gmail.com", Role.TRADER);
    	Order order = new Order(10,company,"B","MARKET",1000.0,678,formatter.parseDateTime("2018-12-05 13:44:44"),user);
    	
    	
    	Response response =
    	given()
    		.contentType("application/json")
    		.body(order)
    	.when()
    		.post("/order")
    	.then()
    		.statusCode(200)
    	.and()	
    		.extract().response();
    	JsonPath jsonPath = new JsonPath(response.body().asString());
    	assertThat(jsonPath.get("orderId"),is(10));
    }
	
	@Test
	public void insertBuyLimitOrder() {
		
	}
	
	@Test
	public void insertSellMarketOrder() {
		
	}
	
	@Test
	public void insertSellLimitOrder() {
		
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
    	//List<String> jsonResponse = response.jsonPath().getList("status");
    	//String cancelledOrder = jsonResponse.get(jsonResponse.size()-1);
    	Order cancelledOrder = orderRepo.findOrderById(1);
    	assertThat(cancelledOrder.getStatus(), is("CANCELLED"));
	 }
	
	@Test
	public void cancelFilledOrder() {
    	try {
    		when().
    			get("/cancel/{orderId}",2).
			then().
    			statusCode(400);
    	}catch(InvalidActionException e) {
    		assertThat(e.getMessage(), is("Order 2 has been filled or cancelled. Unable to cancel order."));
    	}
    }
	 
	@Test
	public void cancelInvalidOrder() {
		try {
			when().
				get("/cancel/{orderId}",100).
			then().
				statusCode(400);
		}catch(InvalidParameterException e) {
			assertThat(e.getMessage(), is("Invalid order id."));
		}
		
	}
	
	@Test
	public void cancelCancelledOrder() {
		try {
			when().
				get("/cancel/{orderId}",3).
			then().
				statusCode(400);
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
