package com.cs.view;

import com.cs.domain.Order;
import com.cs.domain.Transaction;
import org.joda.time.DateTime;

public class TransactionView {


    public int txnId;
    public int userId;
    public String operation;
    public String timeStamp;
    public String stockSymbol;
    public int quantity;
    public double askPrice;
    public String typeOfOrder;

    public TransactionView(Transaction txn, Order order){
        this.txnId = txn.getId();
        this.userId = order.getTrader().getUserId();
        this.operation = txn.getOperation().toString();
        this.timeStamp = txn.getTxnTimeStamp().toString();
        this.timeStamp = order.getCompany().getName();
        this.quantity = txn.getQuantity();
        this.askPrice = txn.getPrice();
        this.typeOfOrder = order.getType();
    }


}
