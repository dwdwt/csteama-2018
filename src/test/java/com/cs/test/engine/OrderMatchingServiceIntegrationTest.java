package com.cs.test.engine;

import static org.hamcrest.MatcherAssert.assertThat;

import com.cs.Csteama2018Application;
import com.cs.domain.*;
import com.cs.service.OrderMatchingService;
import com.cs.service.OrderService;
import com.cs.service.TransactionService;
import com.cs.service.UserService;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class OrderMatchingServiceIntegrationTest {

    @Autowired
    OrderMatchingService orderMatchingService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    Company DB = mock(Company.class);
    Company MS = mock(Company.class);
    User someTrader;
    User someOtherTrader;
    Order buyOrder;
    Order sellOrder;


    @Before
    public void setUp(){
        someTrader = userService.getUserById(1);
        someOtherTrader = userService.getUserById(2);
        when(DB.getTickerSymbol()).thenReturn("DB.HK");
        when(MS.getTickerSymbol()).thenReturn("MS.HK");
        buyOrder = orderService.insertOrder(new Order(DB,"B","LIMIT",50,12,LocalDateTime.now().toDateTime(),someTrader));
    }

    @Test
    public void buyOrderCanMatchSellOrderWithSameQuantityAndOrderStatusWillBeUpdated() throws InterruptedException {
        sellOrder = orderService.insertOrder(new Order(DB,"S","LIMIT",50,12,LocalDateTime.now().toDateTime(),someOtherTrader));

        orderMatchingService.matchOrderWithAny(buyOrder);
        Thread.sleep(1000);
        Order updatedBuyOrder = orderService.findOrderById(buyOrder.getOrderId());
        Order updatedSellOrder = orderService.findOrderById(sellOrder.getOrderId());
        assertEquals(0,updatedBuyOrder.getNoOfShares());
        assertEquals("FILLED",updatedBuyOrder.getStatus());
        assertEquals(0,updatedSellOrder.getNoOfShares());
        assertEquals("FILLED",updatedSellOrder.getStatus());

        assertEquals(Operation.FILL.toString(), transactionService.getAllTransactionViewsByCriteria(String.valueOf(someTrader.getId()),"DB.HK",null,null,null).get(0).operation);
        assertEquals(Operation.FILL.toString(), transactionService.getAllTransactionViewsByCriteria(String.valueOf(someOtherTrader.getId()),"DB.HK",null,null,null).get(0).operation);
    }

    @Test
    public void buyOrderCanMatchTwoOrder() throws InterruptedException {
        sellOrder = orderService.insertOrder(new Order(DB,"S","LIMIT",49,6,LocalDateTime.now().toDateTime(),someOtherTrader));
        Order anotherSellOrder = orderService.insertOrder(new Order(DB,"S","LIMIT",49,1,LocalDateTime.now().toDateTime(),someOtherTrader));

        orderMatchingService.matchOrderWithAny(buyOrder);
        Thread.sleep(1000);

        Order updatedBuyOrder = orderService.findOrderById(buyOrder.getOrderId());
        Order updatedSellOrder = orderService.findOrderById(sellOrder.getOrderId());
        Order updatedAnotherSellOrder = orderService.findOrderById(anotherSellOrder.getOrderId());

        assertEquals(5,updatedBuyOrder.getNoOfShares());
        assertEquals("OPENED",updatedBuyOrder.getStatus());
        assertEquals(0,updatedSellOrder.getNoOfShares());
        assertEquals("FILLED",updatedSellOrder.getStatus());
        assertEquals(0,updatedAnotherSellOrder.getNoOfShares());
        assertEquals("FILLED",updatedAnotherSellOrder.getStatus());

    }

    @Test
    public void buyOrderCanMatchTwoOrderBasedOnBetterPrice() throws InterruptedException {
        sellOrder = orderService.insertOrder(new Order(DB,"S","LIMIT",49,9,LocalDateTime.now().toDateTime(),someOtherTrader));
        Order betterSellOrder = orderService.insertOrder(new Order(DB,"S","LIMIT",48,12,LocalDateTime.now().toDateTime(),someOtherTrader));
        Order worstSellOrder = orderService.insertOrder(new Order(DB,"S","LIMIT",50,12,LocalDateTime.now().toDateTime(),someOtherTrader));

        orderMatchingService.matchOrderWithAny(buyOrder);
        Thread.sleep(1000);

        Order updatedBuyOrder = orderService.findOrderById(buyOrder.getOrderId());
        Order updatedSellOrder = orderService.findOrderById(sellOrder.getOrderId());
        Order updatedBetterSellOrder = orderService.findOrderById(betterSellOrder.getOrderId());

        assertEquals(0,updatedBuyOrder.getNoOfShares());
        assertEquals("FILLED",updatedBuyOrder.getStatus());
        assertEquals(9,updatedSellOrder.getNoOfShares());
        assertEquals("OPENED",updatedSellOrder.getStatus());
        assertEquals(0,updatedBetterSellOrder.getNoOfShares());
        assertEquals("FILLED",updatedBetterSellOrder.getStatus());
        assertEquals(new Double(48), transactionService.getAllTransactionViewsByCriteria(String.valueOf(someTrader.getId()),"DB.HK",null,null,null).get(0).askPrice);

        // fill that unfilled orders for now: the unfilled order is affecting other tests
        Order dummyOrder = orderService.insertOrder(new Order(DB,"B","LIMIT",100,21,LocalDateTime.now().toDateTime(),someOtherTrader));
        orderMatchingService.matchOrderWithAny(dummyOrder);
    }

    @Test
    public void sellOrderCanMatchBuyOrderWithSameQuantityAndOrderStatusWillBeUpdated() throws InterruptedException {
        Order buyOrderAtMS = orderService.insertOrder(new Order(MS,"B","LIMIT",50,12,LocalDateTime.now().toDateTime(),someTrader));
        Order sellOrderAtMS = orderService.insertOrder(new Order(MS,"S","LIMIT",50,12,LocalDateTime.now().toDateTime(),someOtherTrader));
        orderMatchingService.matchOrderWithAny(sellOrderAtMS);
        Thread.sleep(1000);
        Order updatedBuyOrder = orderService.findOrderById(buyOrderAtMS.getOrderId());
        Order updatedSellOrder = orderService.findOrderById(sellOrderAtMS.getOrderId());
        assertEquals(0,updatedBuyOrder.getNoOfShares());
        assertEquals("FILLED",updatedBuyOrder.getStatus());
        assertEquals(0,updatedSellOrder.getNoOfShares());
        assertEquals("FILLED",updatedSellOrder.getStatus());

        assertEquals(Operation.FILL.toString(), transactionService.getAllTransactionViewsByCriteria(String.valueOf(someTrader.getId()),"MS.HK",null,null,null).get(0).operation);
        assertEquals(Operation.FILL.toString(), transactionService.getAllTransactionViewsByCriteria(String.valueOf(someOtherTrader.getId()),"MS.HK",null,null,null).get(0).operation);
    }

    @Test
    public void sellOrderCanMatchTwoBuyOrder() throws InterruptedException {
        Order sellOrderAtMS = orderService.insertOrder(new Order(MS,"S","LIMIT",50,12,LocalDateTime.now().toDateTime(),someOtherTrader));
        Order buyOrderAtMS = orderService.insertOrder(new Order(MS,"B","LIMIT",51,6,LocalDateTime.now().toDateTime(),someTrader));
        Order anotherBuyOrderAtMS = orderService.insertOrder(new Order(MS,"B","LIMIT",551,5,LocalDateTime.now().toDateTime(),someTrader));


        orderMatchingService.matchOrderWithAny(sellOrderAtMS);
        Thread.sleep(1000);

        Order updatedSellOrder = orderService.findOrderById(sellOrderAtMS.getOrderId());
        Order updatedBuyOrder = orderService.findOrderById(buyOrderAtMS.getOrderId());
        Order updatedAnotherBuyOrder = orderService.findOrderById(anotherBuyOrderAtMS.getOrderId());

        assertEquals(0,updatedBuyOrder.getNoOfShares());
        assertEquals("FILLED",updatedBuyOrder.getStatus());
        assertEquals(1,updatedSellOrder.getNoOfShares());
        assertEquals("OPENED",updatedSellOrder.getStatus());
        assertEquals(0,updatedAnotherBuyOrder.getNoOfShares());
        assertEquals("FILLED",updatedAnotherBuyOrder.getStatus());

        // fill that unfilled orders for now: the unfilled order is affecting other tests
        Order dummyOrder = orderService.insertOrder(new Order(MS,"S","LIMIT",1,1,LocalDateTime.now().toDateTime(),someOtherTrader));
        orderMatchingService.matchOrderWithAny(dummyOrder);

    }

    @Test
    public void sellOrderCanMatchTwoBuyOrderBasedOnBetterPrice() throws InterruptedException {
        Order sellOrderAtMS = orderService.insertOrder(new Order(MS,"S","LIMIT",50,12,LocalDateTime.now().toDateTime(),someOtherTrader));
        Order buyOrderAtMS = orderService.insertOrder(new Order(MS,"B","LIMIT",52,6,LocalDateTime.now().toDateTime(),someTrader));

        Order worseBuyOrderAtMS = orderService.insertOrder(new Order(MS,"B","LIMIT",49,6,LocalDateTime.now().toDateTime(),someTrader));
        Order betterBuyOrderAtMS = orderService.insertOrder(new Order(MS,"B","LIMIT",500,10,LocalDateTime.now().toDateTime(),someTrader));


        orderMatchingService.matchOrderWithAny(sellOrderAtMS);
        Thread.sleep(1000);

        Order updatedSellOrder = orderService.findOrderById(sellOrderAtMS.getOrderId());
        Order updatedBuyOrder = orderService.findOrderById(buyOrderAtMS.getOrderId());
        Order updatedBetterBuyOrder = orderService.findOrderById(betterBuyOrderAtMS.getOrderId());
        Order updatedWorseBuyOrder = orderService.findOrderById(worseBuyOrderAtMS.getOrderId());

        assertEquals(0,updatedSellOrder.getNoOfShares());
        assertEquals("FILLED",updatedSellOrder.getStatus());
        assertEquals(0,updatedBetterBuyOrder.getNoOfShares());
        assertEquals("FILLED",updatedBetterBuyOrder.getStatus());
        assertEquals(4,updatedBuyOrder.getNoOfShares());
        assertEquals("OPENED",updatedBuyOrder.getStatus());
        assertEquals(6,updatedWorseBuyOrder.getNoOfShares());
        assertEquals("OPENED",updatedBuyOrder.getStatus());

        // fill that unfilled orders for now: the unfilled order is affecting other tests
        Order dummyOrder = orderService.insertOrder(new Order(MS,"S","LIMIT",1,10,LocalDateTime.now().toDateTime(),someOtherTrader));
        orderMatchingService.matchOrderWithAny(dummyOrder);
    }


}
