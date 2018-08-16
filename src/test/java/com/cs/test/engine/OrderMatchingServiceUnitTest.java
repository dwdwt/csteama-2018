package com.cs.test.engine;

import com.cs.Csteama2018Application;
import com.cs.domain.*;
import com.cs.service.OrderMatchingService;
import com.cs.service.OrderService;
import com.cs.service.TransactionService;
import com.jayway.jsonpath.Criteria;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderMatchingServiceUnitTest {

    OrderMatchingService orderMatchingService;

    OrderService orderService = mock(OrderService.class);
    TransactionService transactionService = mock(TransactionService.class);

    public Order mockBuyOrder = mock(Order.class);
    public Order mockSellOrder = mock(Order.class);
    public Company mockCompany = mock(Company.class);
    public User mockTrader = mock(User.class);


    @Before
    public void setUp(){
        orderMatchingService = new OrderMatchingService(orderService,transactionService);

        when(mockCompany.getTickerSymbol()).thenReturn("GOD.HK");

        when(mockBuyOrder.getOrderId()).thenReturn(2000);
        when(mockBuyOrder.getCompany()).thenReturn(mockCompany);
        when(mockBuyOrder.getSide()).thenReturn("B");
        when(mockBuyOrder.getType()).thenReturn("LIMIT");
        when(mockBuyOrder.getNoOfShares()).thenReturn(12);
        when(mockBuyOrder.getPrice()).thenReturn(50.0);
        when(mockBuyOrder.getStatus()).thenReturn("OPENED");
        when(mockBuyOrder.getTimeStamp()).thenReturn(new DateTime("2018-01-02"));

        when(mockSellOrder.getOrderId()).thenReturn(2001);
        when(mockSellOrder.getTrader()).thenReturn(mockTrader);
        when(mockSellOrder.getCompany()).thenReturn(mockCompany);
        when(mockSellOrder.getSide()).thenReturn("S");
        when(mockSellOrder.getType()).thenReturn("LIMIT");
        when(mockSellOrder.getNoOfShares()).thenReturn(12);
        when(mockSellOrder.getPrice()).thenReturn(50.0);
        when(mockSellOrder.getStatus()).thenReturn("OPENED");
        when(mockSellOrder.getTimeStamp()).thenReturn(new DateTime("2018-01-02"));
    }



    @Test
    @Ignore
    public void canMatchTwoOrdersAndFillAndPriceWillBeUpdatedWithSamePrice(){

        when (orderService.getAllOppositeOrder(mockBuyOrder)).thenReturn(Arrays.asList(mockSellOrder));

        orderMatchingService.matchOrderWithAny(mockBuyOrder);

        verify(mockBuyOrder).setNoOfShares(0);
        verify(mockSellOrder).setNoOfShares(0);
        verify(transactionService).addTxnRecord(mockBuyOrder, Operation.FILL,mockBuyOrder.getPrice(),mockBuyOrder.getNoOfShares(), any(DateTime.class));
        verify(transactionService).addTxnRecord(mockSellOrder, Operation.FILL,mockSellOrder.getPrice(),mockSellOrder.getNoOfShares(), any(DateTime.class));
        verify(orderService).updateOrder(mockBuyOrder);
        verify(orderService).updateOrder(mockSellOrder);
    }

}
