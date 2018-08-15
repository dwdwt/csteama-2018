package com.cs.view;

import com.cs.domain.Order;
import com.cs.domain.Transaction;
import com.cs.exception.InvalidParameterException;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionView {


    public Integer txnId;
    public Integer userId;
    public String operation;
    public String timeStamp;
    public String stockSymbol;
    public Integer quantity;
    public Double askPrice;
    public String typeOfOrder;

    public TransactionView(Transaction txn, Order order){
        this.txnId = txn.getId();
        this.userId = order.getTrader().getId();
        this.operation = txn.getOperation().toString();
        this.timeStamp = txn.getTxnTimeStamp().toString();
        this.stockSymbol = order.getCompany().getTickerSymbol();
        this.quantity = txn.getQuantity();
        this.askPrice = txn.getPrice();
        this.typeOfOrder = order.getType();
    }

    public TransactionView(Transaction txn, Order order, String[] selectedItems) {
        for (String selectedItem : selectedItems) {
            switch (selectedItem) {
                case "txnId":
                    this.txnId = txn.getId();
                    break;
                case "userId":
                    this.userId = order.getTrader().getId();
                    break;
                case "operation":
                    this.operation = txn.getOperation().toString();
                    break;

                case "timeStamp":
                    this.timeStamp = txn.getTxnTimeStamp().toString();
                    break;

                case "stockSymbol":
                    this.stockSymbol = order.getCompany().getTickerSymbol();
                    break;

                case "quantity":
                    this.quantity = txn.getQuantity();
                    break;

                case "askPrice":
                    this.askPrice = txn.getPrice();
                    break;

                case "typeOfOrder":
                    this.typeOfOrder = order.getType();
                    break;

                default:
                    throw new InvalidParameterException("No Such Filter: " + selectedItem);
            }
        }
    }


}
