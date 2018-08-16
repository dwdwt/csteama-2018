package com.cs.test.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.dao.QuoteRepository;
import com.cs.domain.Quote;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Csteama2018Application.class})
public class QuoteRepoIntegrationTest {

    @Autowired
    QuoteRepository quoteRepo;

    //Story 9 Tests
    @Test
    public void listQuotesWithTickerSymbol() {
    	assertThat(quoteRepo.filterAndSortQuotesByCriteria("HIJ.HK","","","asc").size(), is(6));
    }
    
    @Test
    public void listQuotesWithValidFromTimestampAndToTimestamp() {
    	List<Integer> data = new ArrayList<>();
    	List<Quote> raw = quoteRepo.filterAndSortQuotesByCriteria("", "2018-08-16 10:17:20","2018-08-16 10:17:30","asc");
    	for(Quote quote: raw) {
    		data.add(quote.getBuyOrder().getOrderId());
    	}
    	
    	List<Integer> answer = new ArrayList<Integer>(Arrays.asList(new Integer[] {23,25}));
    	assertThat(data, is(answer));
    }
    
    @Test
    public void listQuotesWithValidFromTimestampAndToTimestampAndDescendingSortSeq() {
    	List<Integer> data = new ArrayList<>();
    	List<Quote> raw = quoteRepo.filterAndSortQuotesByCriteria("", "2018-08-16 10:17:20","2018-08-16 10:17:30","desc");
    	for(Quote quote: raw) {
    		data.add(quote.getBuyOrder().getOrderId());
    	}
    	
    	List<Integer> answer = new ArrayList<Integer>(Arrays.asList(new Integer[] {25,23}));
    	assertThat(data, is(answer));
    }
 
}
