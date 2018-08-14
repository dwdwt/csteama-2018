package com.cs.test.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.dao.OrderRepository;
import com.cs.domain.Company;
import com.cs.domain.Industry;
import com.cs.domain.Order;
import com.cs.domain.Role;
import com.cs.domain.User;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = {Csteama2018Application.class})
public class OrderRepoIntegrationTest {

    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    private int serverPort;
    
    @Before
    public void init() {
    	RestAssured.port=serverPort;
    }
    @Test
    public void canFindAllOrders() {
    	assertThat(orderRepository.findAllOrders().size(), is(8));
    }

 
    
    
    @Test
    public void canFindByOrderId() {
    	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
    	Industry industry = new Industry("IT Services","Services");
    	Company company = new Company("ABC.HK","ABC CO HONG KONG", industry);
    	User user = new User(1,"Jon","Doe", "1234","jondoe@gmail.com", Role.TRADER);
    	assertThat(orderRepository.findOrderById(1), samePropertyValuesAs(new Order(1,company,"B","LIMIT",10.0,5,formatter.parseDateTime("16/08/2018 10:17:23"),user,"OPENED")));
    }
    
    //TODO
    @Test
    public void listOrdersGroupByOrderSideOrderTypeOrderStatus() {
    	
    	assertThat(orderRepository.filterAndSortOrdersByCriteria());
    }
   
    @Test
    public void listOrdersBasedOnBuy() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "B");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"","").size(), is(4));
    }
    
    @Test
    public void listOrdersBasedOnSell() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "S");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"","").size(), is(4));
    }
    
    @Test
    public void listOrdersBasedOnBuyLimit() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "B");
    	criteriaMap.put("orderType", "LIMIT");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"","").size(), is(3));
    }
    
    @Test
    public void listOrdersBasedOnSellLimit() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "S");
    	criteriaMap.put("orderType", "LIMIT");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"","").size(), is(3));
    }
    
    @Test
    public void listOrdersBasedOnBuyMarketOpen() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "B");
    	criteriaMap.put("orderType", "MARKET");
    	criteriaMap.put("status", "OPEN");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"","").size(), is(0));
    }
    
    @Test
    public void listOrdersBasedOnBuyLimitCancelledTicker() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "B");
    	criteriaMap.put("orderType", "LIMIT");
    	criteriaMap.put("status", "CANCELLED");
    	criteriaMap.put("tickerSymbol", "HIJ.HK");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"","").size(), is(2));
    }
    
    @Test
    public void listOrdersBasedOnBuyLimitCancelledTickerFromToQty() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "B");
    	criteriaMap.put("orderType", "LIMIT");
    	criteriaMap.put("status", "CANCELLED");
    	criteriaMap.put("tickerSymbol", "HIJ.HK");
    	criteriaMap.put("fromOrderQty", "15");
    	criteriaMap.put("toOrderQty", "30");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"","").size(), is(1));
    }
    
    @Test
    public void listOrdersBasedOnBuyLimitCancelledTickerFromToQtyFromToTimestamp() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "B");
    	criteriaMap.put("orderType", "LIMIT");
    	criteriaMap.put("status", "CANCELLED");
    	criteriaMap.put("tickerSymbol", "HIJ.HK");
    	criteriaMap.put("fromOrderQty", "15");
    	criteriaMap.put("toOrderQty", "30");
    	criteriaMap.put("fromTimestamp", "2018-08-16 10:50:00");
    	criteriaMap.put("toTimestamp", "2018-08-16 11:50:00");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"","").size(), is(1));
    }
    
    @Test
    public void listOrdersBasedOnBuyLimitCancelledTickerFromToQtyFromToTimestampSortParamsSortOrder() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "B");
    	criteriaMap.put("orderType", "LIMIT");
    	criteriaMap.put("status", "CANCELLED");
    	criteriaMap.put("tickerSymbol", "HIJ.HK");
    	criteriaMap.put("fromOrderQty", "15");
    	criteriaMap.put("toOrderQty", "30");
    	criteriaMap.put("fromTimestamp", "2018-08-16 10:50:00");
    	criteriaMap.put("toTimestamp", "2018-08-16 11:50:00");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"tickerSymbol,price","asc").size(), is(1));
    }
    
    @Test
    public void listOrdersBasedOnBuyLimitCancelledTickerFromToQtyFromToTimestampSortParams() {
    	HashMap<String,String> criteriaMap = new HashMap<String,String>();
    	criteriaMap.put("side", "B");
    	criteriaMap.put("orderType", "LIMIT");
    	criteriaMap.put("status", "CANCELLED");
    	criteriaMap.put("tickerSymbol", "HIJ.HK");
    	criteriaMap.put("fromOrderQty", "15");
    	criteriaMap.put("toOrderQty", "30");
    	criteriaMap.put("fromTimestamp", "2018-08-16 10:50:00");
    	criteriaMap.put("toTimestamp", "2018-08-16 11:50:00");
    	assertThat(orderRepository.filterAndSortOrdersByCriteria(criteriaMap,"tickerSymbol,price","").size(), is(1));
    }
    /*
    @Test
    public void allOrdersReturned() {
    	Response response =
    		when()
    			.get("/orders?side=B").
    		then()
    			.statusCode(200).
    		and()
    			.extract().response();
    	Order[] jsonResponse = response.as(Order[].class);
    	assertThat(jsonResponse.length, is(4));
    }*/
    

}
