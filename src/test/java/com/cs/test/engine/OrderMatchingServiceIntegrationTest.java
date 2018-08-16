package com.cs.test.engine;

import com.cs.Csteama2018Application;
import com.cs.domain.Company;
import com.cs.domain.Order;
import com.cs.domain.Status;
import com.cs.domain.User;
import com.cs.service.OrderMatchingService;
import com.cs.service.OrderService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class OrderMatchingServiceIntegrationTest {

    @Autowired
    OrderMatchingService orderMatchingService;

    @Autowired
    OrderService orderService;

    public Order mockBuyOrder = mock(Order.class);
    public Order mockSellOrder = mock(Order.class);
    public Company mockCompany = mock(Company.class);
    public User mockTrader = mock(User.class);

    @Before
    public void setUp(){
        when(mockTrader.getId()).thenReturn(9999);
        when(mockBuyOrder.getTrader()).thenReturn(mockTrader);
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
        when(mockSellOrder.getPrice()).thenReturn(50.1);
        when(mockSellOrder.getStatus()).thenReturn("OPENED");
        when(mockSellOrder.getTimeStamp()).thenReturn(new DateTime("2018-01-02"));
    }



    //TODO: not passing yet
    @Test
    public void canMatchTwoOrders(){
        orderService.insertOrder(mockBuyOrder);
        orderService.insertOrder(mockSellOrder);

        orderMatchingService.matchAll();

        Order buyOrder =  orderService.findOrderById(2000);
        Order sellOrder =  orderService.findOrderById(2001);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(buyOrder.getStatus(), is(Status.FUFILLED.toString()));
        assertThat(buyOrder.getNoOfShares(),is(0));
        assertThat(sellOrder.getStatus(),is(Status.FUFILLED.toString()));
        assertThat(sellOrder.getNoOfShares(),is(0));

    }

}
