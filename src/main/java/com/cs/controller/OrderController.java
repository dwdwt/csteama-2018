package com.cs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.view.TraderOrderView;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.domain.Company;
import com.cs.domain.Order;
import com.cs.exception.InvalidActionException;
import com.cs.exception.InvalidOrderException;
import com.cs.exception.InvalidParameterException;
import com.cs.service.CompanyService;
import com.cs.service.OrderService;

@RestController
public class OrderController {


    @Autowired
    OrderService orderSvc;

    @Autowired
    CompanyService companySvc;

    @RequestMapping("/orders")
    public List<Order> findOrdersWithFilterAndSortingCriteria(
            @RequestParam(value = "side", defaultValue = "") String side,
            @RequestParam(value = "orderType", defaultValue = "") String type,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "tickerSymbol", defaultValue = "") String tickerSymbol,
            @RequestParam(value = "fromOrderQty", defaultValue = "") String fromOrderQty,
            @RequestParam(value = "toOrderQty", defaultValue = "") String toOrderQty,
            @RequestParam(value = "fromTimestamp", defaultValue = "") String fromTimestamp,
            @RequestParam(value = "toTimestamp", defaultValue = "") String toTimestamp,
            @RequestParam(value = "sort", defaultValue = "") String sortParams,
            @RequestParam(value = "sortSeq", defaultValue = "") String sortSequence) {

        Map<String, String> criteriaMap = new HashMap<>();

        // Parameter Validation
        if (!side.isEmpty()) {
            if (!side.matches("B|S")) {
                throw new InvalidParameterException("Invalid side parameter. Input only B or S.");
            } else {
                criteriaMap.put("side", side);
            }
        }

        if (!type.isEmpty()) {
            if (!type.matches("MARKET|LIMIT")) {
                throw new InvalidParameterException("Invalid order type parameter. Input only MARKET or LIMIT.");
            } else {
                criteriaMap.put("orderType", type);
            }
        }

        if (!status.isEmpty()) {
            if (!status.matches("OPENED|FILLED|CANCELLED")) {
                throw new InvalidParameterException("Invalid order status parameter. Input only OPENED, FILLED or CANCELLED.");
            } else {
                criteriaMap.put("status", status);
            }
        }

        if (!tickerSymbol.isEmpty()) {
            try {
                Company toFind = companySvc.findCompanyByTickerSymbol(tickerSymbol);
                criteriaMap.put("tickerSymbol", tickerSymbol);
            } catch (EmptyResultDataAccessException e) {
                throw new InvalidParameterException("Ticker symbol does not exist.");
            }
        }

        if (!fromOrderQty.isEmpty() && !toOrderQty.isEmpty()) {
            int fromQty = -1;
            int toQty = -1;
            try {
                fromQty = Integer.parseInt(fromOrderQty);

            } catch (NumberFormatException e) {
                throw new InvalidParameterException("Invalid from order quantity. Input only integers.");
            }

            try {
                toQty = Integer.parseInt(toOrderQty);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException("Invalid to order quantity. Input only integers.");
            }

            if (fromQty >= 0 && toQty >= 0) {
                if (toQty < fromQty) {
                    throw new InvalidParameterException("To order quantity cannot be smaller than From order quantity.");
                } else {
                    criteriaMap.put("fromOrderQty", fromOrderQty);
                    criteriaMap.put("toOrderQty", toOrderQty);
                }
            }
        } else if (!fromOrderQty.isEmpty() || !toOrderQty.isEmpty()) {
            throw new InvalidParameterException("Invalid order quantity parameters. Both from and to order quantities must be provided.");
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime from = null;
        if (!fromTimestamp.isEmpty()) {
            try {
                from = formatter.parseDateTime(fromTimestamp);
            } catch (IllegalArgumentException e) {
                throw new InvalidParameterException("Invalid from timestamp. Input timestamp in yyyy-MM-dd HH:mm:ss format only.");
            }
        }

        DateTime to = null;
        if (!toTimestamp.isEmpty()) {
            try {
                to = formatter.parseDateTime(toTimestamp);
            } catch (IllegalArgumentException e) {
                throw new InvalidParameterException("Invalid to timestamp. Input timestamp in yyyy-MM-dd HH:mm:ss format only.");
            }
        }

        if (to != null && from != null) {
            if (to.isBefore(from)) {
                throw new InvalidParameterException("To timestamp cannot be before From timestamp.");
            } else {
                criteriaMap.put("fromTimestamp", fromTimestamp);
                criteriaMap.put("toTimestamp", toTimestamp);
            }
        } else if (to != null || from != null) {
            throw new InvalidParameterException("Invalid timestamp parameters. Both from and to timestamps must be provided.");
        }

        if (!sortParams.isEmpty() && !sortSequence.isEmpty()) {
            if (!sortParams.contains("tickerSymbol") && !sortParams.contains("price")) {
                throw new InvalidParameterException("Sorting parameters only include tickerSymbol and price.");
            }

            if (!sortSequence.contains("asc") && !sortSequence.contains("desc")) {
                throw new InvalidParameterException("Sorting sequence only allows asc and desc.");
            }
        } else if (!sortParams.isEmpty() || !sortSequence.isEmpty()) {
            throw new InvalidParameterException("Invalid sorting parameters. Both sort and sortSeq must be provided.");
        }

        return orderSvc.filterAndSortOrdersByCriteria(criteriaMap, sortParams, sortSequence);
    }

    @RequestMapping("/cancel/{orderId}")
    public List<Order> cancelOrder(@PathVariable("orderId") int orderId) {
        try {
            Order order = orderSvc.findOrderById(orderId);

            if (!order.getStatus().matches("FILLED|CANCELLED")) {
                orderSvc.cancelOrder(orderId);
            } else {
                throw new InvalidActionException("Order " + orderId + " has been filled or cancelled. Unable to cancel order.");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidParameterException("Invalid order id.");
        }
        return orderSvc.findAllOrders();
    }


    //find orders by userid
    @RequestMapping("/orders/{userId}")
    public List<Order> findOrdersByUserId(@PathVariable("userId") int uid) {
        return orderSvc.getOrdersByUserId(uid);
    }

    //last time stamp, total number of orders
    @RequestMapping("/orders/{userId}/summary")
    public TraderOrderView listOrderSummaryByTrader(@PathVariable("userId") int uid) {
        return new TraderOrderView(
                orderSvc.getLastOrderTimestamp(uid),
                orderSvc.getTotalOrdersBystatus(uid, "OPENED"),
                orderSvc.getTotalOrdersBystatus(uid, "FILLED"),
                orderSvc.getTotalOrdersBystatus(uid, "CANCELLED")
        );

    }
    


    @RequestMapping("/update/{orderId}")
    public List<Order> updateOrder(@PathVariable("orderId") int orderId,
                                   @RequestParam(value = "quantity", defaultValue = "") String noOfShares,
                                   @RequestParam(value = "price", defaultValue = "") String sharePrice,
                                   @RequestParam(value = "orderType", defaultValue = "") String orderType) {

        if (noOfShares.isEmpty() && sharePrice.isEmpty() && orderType.isEmpty()) {
            throw new InvalidParameterException("No update parameters found.");
        }

        Integer shares = null;
        if (!noOfShares.isEmpty()) {
            try {
                shares = Integer.parseInt(noOfShares);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException("Number of shares has to be an integer value.");
            }
        }

        Double price = null;
        if (!sharePrice.isEmpty()) {
            try {
                price = Double.parseDouble(sharePrice);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException("Price has to be a double value.");
            }
        }

        if (!orderType.isEmpty() && !orderType.matches("MARKET|LIMIT")) {
            throw new InvalidParameterException("Order type has to be either MARKET or LIMIT.");
        }

        Map<String, Object> updateMap = new HashMap<>();
        if (!noOfShares.isEmpty()) updateMap.put("noOfShares", shares);
        if (!sharePrice.isEmpty()) updateMap.put("price", price);
        if (!orderType.isEmpty()) updateMap.put("orderType", orderType);

        try {
            Order order = orderSvc.findOrderById(orderId);

            if (order.getStatus().matches("FILLED|CANCELLED")) {
                throw new InvalidActionException("Order " + orderId + " has been filled/cancelled. Unable to update order.");
            }

            orderSvc.updateOrder(orderId, updateMap);
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidParameterException("Invalid order id.");
        }
        return orderSvc.findAllOrders();
    }

    @RequestMapping(value="/order", method = RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Order insertOrder(@RequestBody Order order){
	try {
		if(order.getNoOfShares() <= 0 || order.getPrice() < 0) {
			throw new InvalidOrderException("Quantiy of price and shares must be positive.");
		} else if (order.getType().equals("MARKET") && order.getPrice() > 0) {
			throw new InvalidOrderException("Unable to specify price when order type is market.");
		}
		Company company = companySvc.findCompanyByTickerSymbol(order.getCompany().getTickerSymbol());
		
	}catch(EmptyResultDataAccessException e) {
		throw new InvalidOrderException("Invalid company");
	}
		order.setStatus("OPENED");
		return orderSvc.insertOrder(order);
	}
	
	
	
	
	

}
