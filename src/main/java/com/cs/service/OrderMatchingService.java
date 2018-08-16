package com.cs.service;

import com.cs.domain.Operation;
import com.cs.domain.Order;
import com.cs.domain.Quote;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderMatchingService {

    @Autowired
    OrderService orderService;

    @Autowired
    QuoteService quoteService;

    @Autowired
    TransactionService transactionService;

    LocalDateTime localDateTime = new LocalDateTime();

    public OrderMatchingService(OrderService orderService, TransactionService transactionService) {
        this.orderService = orderService;
        this.transactionService = transactionService;
    }


    public void matchOrderWithAny(Order order) {
        List<Order> allOppositeOrders = orderService.getAllOppositeOrder(order);
        if (allOppositeOrders.size() > 0){
            if (order.getSide().equals("B"))
                Collections.sort(allOppositeOrders, Comparator.comparingDouble(Order::getPrice));
            else Collections.sort(allOppositeOrders, Comparator.comparingDouble(Order::getPrice).reversed());

            for (Order oppositeOrder: allOppositeOrders){
                if (order.getNoOfShares() == 0) break;
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
                order.setStatus("FILLED");
                oppositeOrder.setNoOfShares(oppositeOrder.getNoOfShares()-tradedQuantity);
            }
            else {
                order.setNoOfShares(remainder);
                oppositeOrder.setNoOfShares(0);
                oppositeOrder.setStatus("FILLED");
            }

            //update
            if (order.getNoOfShares() == 0) order.setStatus("FILLED");
            if (oppositeOrder.getNoOfShares() == 0) oppositeOrder.setStatus("FILLED");

            DateTime now = LocalDateTime.now().toDateTime();


            transactionService.addTxnRecord(order,Operation.FILL,tradedPrice,tradedQuantity, now);
            transactionService.addTxnRecord(oppositeOrder,Operation.FILL,tradedPrice,tradedQuantity,now);

            quoteService.addQuote(new Quote(isBuyOrder? order:oppositeOrder,isBuyOrder? oppositeOrder:order, tradedQuantity,now));

            orderService.updateOrder(order);
            orderService.updateOrder(oppositeOrder);
            return true;
        }
        return false;
    }
}
