package com.cs.test.service;

import com.cs.Csteama2018Application;
import com.cs.domain.*;
import com.cs.service.TransactionService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class TransactionServiceIntegrationTest {

    @Autowired
    TransactionService transactionService;

    @Test
    public void canAddTransaction(){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime someDateTime = formatter.parseDateTime("2008-11-11 13:23:44");
        Order someOrder = mock(Order.class);
        when(someOrder.getOrderId()).thenReturn(100);

        transactionService.addTxnRecord(someOrder,Operation.OPEN,10.1,2,someDateTime);

        List<Integer> orderIdList = new ArrayList<Integer>();
        transactionService.getAllTransactions().forEach(it -> orderIdList.add(it.getOrderId()));
        assertThat(orderIdList, hasItem(100));
    }

}
