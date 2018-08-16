package com.cs.test.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.domain.Operation;
import com.cs.domain.Order;
import com.cs.service.TransactionService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class TransactionServiceIntegrationTest {

    @Autowired
    TransactionService transactionService;

    @Test
    public void canAddTransaction(){
        Order someOrder = mock(Order.class);
        when(someOrder.getOrderId()).thenReturn(100);

        transactionService.addTxnRecord(someOrder,Operation.OPEN,10.1,2, LocalDateTime.now().toDateTime());

        List<Integer> orderIdList = new ArrayList<Integer>();
        transactionService.getAllTransactions().forEach(it -> orderIdList.add(it.getOrderId()));
        assertThat(orderIdList, hasItem(100));
    }

}
