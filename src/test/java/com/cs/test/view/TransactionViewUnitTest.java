package com.cs.test.view;

import com.cs.domain.*;
import com.cs.exception.InvalidParameterException;
import com.cs.view.TransactionView;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionViewUnitTest {

	@Test (expected = InvalidParameterException.class)
	public void willThrowExceptionWhenFilterContainsUnknownThing(){
		Transaction txn = new Transaction(1,1,Operation.OPEN,1.0,1,null);
		Order someOrder = mock(Order.class);
		User someUser = mock(User.class);
		Company someCompany = mock(Company.class);
		when(someOrder.getTrader()).thenReturn(someUser);
		when(someOrder.getType()).thenReturn("someType");
		when(someUser.getId()).thenReturn(123);
		when(someCompany.getTickerSymbol()).thenReturn("someTickerSymbol");
		new TransactionView(txn,someOrder, new String[]{"txnId", "someDummyField"});
	}

}
