package com.cs.test.view;

import com.cs.Csteama2018Application;
import com.cs.dao.OrderRepository;
import com.cs.domain.*;
import com.cs.view.TransactionView;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionViewUnitTest {

	@Test (expected = IllegalArgumentException.class)
	public void willThrowExceptionWhenFilterContainsUnknownThing(){
		Transaction txn = new Transaction(1,1,Operation.OPEN,1.0,1,null);
		Order someOrder = mock(Order.class);
		User someUser = mock(User.class);
		Company someCompany = mock(Company.class);
		when(someOrder.getTrader()).thenReturn(someUser);
		when(someOrder.getType()).thenReturn("someType");
		when(someUser.getUserId()).thenReturn(123);
		when(someCompany.getTickerSymbol()).thenReturn("someTickerSymbol");
		new TransactionView(txn,someOrder, new String[]{"txnId", "someDummyField"});
	}

}
