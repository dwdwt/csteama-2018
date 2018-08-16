package com.cs.test.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class OrderRepoIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    //Story 4 Tests
    @Test
    public void canFindAllOrders() {
    	assertThat(orderRepository.findAllOrders().size(), is(8));
    }

    @Test
    public void canFindByOrderId() {
    	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    	Industry industry = new Industry("IT Services","Services");
    	Company company = new Company("ABC.HK","CS", industry);
    	User user = new User(1,"Jon","Doe", "1234","jondoe@gmail.com", Role.TRADER,"smu");
    	assertThat(orderRepository.findOrderById(1), samePropertyValuesAs(new Order(1,company,"B","LIMIT",10.0,5,formatter.parseDateTime("2018-08-16 10:17:23"),user,"OPENED")));
    }
    
//    //TODO
//    @Test
//    public void listOrdersGroupByOrderSideOrderTypeOrderStatus() {
//
//    	assertThat(orderRepository.filterAndSortOrdersByCriteria());
//    }
   
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
    
    @Test
    public void cancelExistingOrder() {
    	orderRepository.cancelOrder(1);
    	Order order = orderRepository.findOrderById(1);
    	assertThat(order.getStatus(), is("CANCELLED"));
    }
    
    @Test
    public void updateExistingOrderNumberOfShares() {
    	HashMap<String,Object> updateMap = new HashMap<String,Object>();
    	updateMap.put("noOfShares", new Integer(1000));
    	orderRepository.updateOrder(1, updateMap);;
    	Order order = orderRepository.findOrderById(1);
    	assertThat(order.getNoOfShares(), is(1000));
    }
    
    @Test
    public void updateExistingOrderNumberOfSharesAndPrice() {
    	HashMap<String,Object> updateMap = new HashMap<String,Object>();
    	updateMap.put("noOfShares", new Integer(800));
    	updateMap.put("price", new Double(20));
    	orderRepository.updateOrder(1, updateMap);;
    	Order order = orderRepository.findOrderById(1);
    	assertThat(order.getNoOfShares(), is(800));
    	assertThat(order.getPrice(), is(20.0));
    }
    
    @Test
    public void updateExistingOrderNumberOfSharesPriceAndType() {
    	HashMap<String,Object> updateMap = new HashMap<String,Object>();
    	updateMap.put("noOfShares", new Integer(300));
    	updateMap.put("price", new Double(60));
    	updateMap.put("orderType", "LIMIT");
    	orderRepository.updateOrder(1, updateMap);;
    	Order order = orderRepository.findOrderById(1);
    	assertThat(order.getNoOfShares(), is(300));
    	assertThat(order.getPrice(), is(60.0));
    	assertThat(order.getType(), is("LIMIT"));
    }


    @Test
    public void updateExistingOrderWithoutParameters() {
    	HashMap<String,Object> updateMap = null;
    	orderRepository.updateOrder(1, updateMap);;
    	Order order = orderRepository.findOrderById(1);
    	assertThat(order.getNoOfShares(), is(5));
    	assertThat(order.getPrice(), is(10.0));
    	assertThat(order.getType(), is("LIMIT"));
    }

  //Story 1 Tests
  	@Test
  	public void insertBuyMarketOrder() {
  		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		Industry industry = new Industry("Commodities Trading","Commodities Services");
    	Company company = new Company("HIJ.HK","CS", industry);
    	User user = new User(10,"Jane","Dong", "4321","janedong@gmail.com", Role.TRADER,"smu");
    	Order order = new Order(company,"B","MARKET",1000.0,678,formatter.parseDateTime("2018-12-05 13:44:44"),user);
  		Order insertedOrder = orderRepository.insertOrder(order);
  		assertThat(insertedOrder,samePropertyValuesAs(order));
  	}

  	@Test
  	public void insertInvalidBuyMarketOrder() {
  		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		Industry industry = new Industry("Commodities Trading","Commodities Services");
    	Company company = new Company("HIJ.HK","CS", industry);
    	User user = new User(10,"Jane","Dong", "4321","janedong@gmail.com", Role.TRADER,"smu");
    	Order order = new Order(company,"B","MARKET",-1000.0,678,formatter.parseDateTime("2018-12-05 13:44:44"),user);
  		Order insertedOrder = orderRepository.insertOrder(order);
  		assertThat(insertedOrder,samePropertyValuesAs(order));
  	}



//    @Test
//
//    public void updateExistingOrderWithoutParameters() {
//    	HashMap<String,Object> updateMap = null;
//    	orderRepository.updateOrder(1, updateMap);;
//    	Order order = orderRepository.findOrderById(1);
//    	assertThat(order.getNoOfShares(), is(5));
//    	assertThat(order.getPrice(), is(10.0));
//    	assertThat(order.getType(), is("LIMIT"));
//    }
//
//  //Story 1 Tests
//  	@Test
//  	public void insertBuyMarketOrder() {
//  		orderRepository.insertOrder();
//  	}
//
//  	@Test
//  	public void insertBuyLimitOrder() {
//  		orderRepository.insertOrder();
//  	}
//
//  	@Test
//  	public void insertSellMarketOrder() {
//  		orderRepository.insertOrder();
//  	}
//
//  	@Test
//  	public void insertSellLimitOrder() {
//  		orderRepository.insertOrder();
//  	}
//

	@Test
	public void canGetTimeStampOfLastOrder() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		assertThat(orderRepository.findLastOrderTimestamp(3), is(formatter.parseDateTime(("2018-08-16 10:57:23"))));
	}


	@Test
	public void canGetTotalNumberOfOrdersByStatus() {
		assertThat(orderRepository.getOrderCountByStatus(3,"OPENED"), is(1));
		assertThat(orderRepository.getOrderCountByStatus(3,"CANCELLED"), is(2));
		assertThat(orderRepository.getOrderCountByStatus(2,"FILLED"), is(2));
	}

	@Test
	public void canGetTopfiveByNumberofTrades() {
		List<Integer> list = Arrays.asList(4,6,5,3,2);
		assertEquals(orderRepository.getTopfiveByNumberofTrades(), list);
	}
	




}
