package com.cs.test.dao;

import static com.cs.domain.Operation.OPEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.dao.TransactionRepository;
import com.cs.domain.Transaction;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class TransactionRepoIntegrationTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void canFindAllTransactions() {
        assertThat(transactionRepository.findAll().size(), is(3));
    }

    @Test
    public void canFindByTransactionId() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        assertThat(transactionRepository.findById(1), samePropertyValuesAs(new Transaction(1,1,OPEN,10.0,5,formatter.parseDateTime("2008-11-11 13:23:44"))));
    }

    @Test
    public void canInsertTransaction() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        Transaction txn = new Transaction(4,1,OPEN,10.0,5,formatter.parseDateTime("2008-11-11 13:23:44"));
        transactionRepository.insertTxn(txn);
        assertThat(transactionRepository.findById(txn.getId()), samePropertyValuesAs(txn));
    }

}
