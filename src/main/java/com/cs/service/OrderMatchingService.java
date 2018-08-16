package com.cs.service;

import com.cs.domain.Operation;
import com.cs.domain.Order;
import org.apache.tomcat.jni.Local;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderMatchingService {

    @Autowired
    OrderService orderService;

    @Autowired
    TransactionService transactionService;

    LocalDateTime localDateTime = new LocalDateTime();

    public OrderMatchingService(OrderService orderService, TransactionService transactionService) {
        this.orderService = orderService;
        this.transactionService = transactionService;
    }


    public void matchOrderWithAny(Order order) {
        Map<String, String> criteriaMap = new HashMap<>();
        criteriaMap.put("stockSymbol", order.getCompany().getTickerSymbol());
        List<Order> allOppositeOrders = orderService.getAllOppositeOrder(order);

        if (allOppositeOrders.size() > 0){
            if (order.getSide().equals("B"))
                Collections.sort(allOppositeOrders, Comparator.comparingDouble(Order::getPrice));
            else Collections.sort(allOppositeOrders, Comparator.comparingDouble(Order::getPrice).reversed());

            for (Order oppositeOrder: allOppositeOrders){
                if(order.getNoOfShares() == 0) break;
                if (!tryMatchAndUpdate(order, oppositeOrder)) return;
            }
        }
    }

    private boolean tryMatchAndUpdate(Order order, Order oppositeOrder) {
        boolean isBuyOrder = order.getSide().equals("B");
        if (isBuyOrder ? order.getPrice() >= oppositeOrder.getPrice() : order.getPrice() <= oppositeOrder.getPrice()){
            int tradedQuantity = Math.min(order.getNoOfShares(),oppositeOrder.getNoOfShares());
            double tradedPrice = isBuyOrder ? oppositeOrder.getPrice() : order.getPrice();
            int remainder = order.getNoOfShares() - tradedQuantity;
            if (remainder == 0) {
                order.setNoOfShares(0);
                oppositeOrder.setNoOfShares(oppositeOrder.getNoOfShares()-tradedQuantity);
            }
            else {
                order.setNoOfShares(order.getNoOfShares() - tradedQuantity);
                oppositeOrder.setNoOfShares(0);
            }

            transactionService.addTxnRecord(order,Operation.FILL,tradedPrice,tradedQuantity);
            transactionService.addTxnRecord(oppositeOrder,Operation.FILL,tradedPrice,tradedQuantity);
            orderService.updateOrder(order);
            orderService.updateOrder(oppositeOrder);
            return true;
        }
        return false;
    }
}
