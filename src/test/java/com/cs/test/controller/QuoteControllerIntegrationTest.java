package com.cs.test.controller;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { Csteama2018Application.class })
public class QuoteControllerIntegrationTest {
	@LocalServerPort
	private int serverPort;

	@Before
	public void init() {
		RestAssured.port = serverPort;
	}

	// Story 9 Tests
	@Test
	public void listQuotesBasedOnSymbol() {
		Response response = when().get("/quotes?tickerSymbol=HIJ.HK").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		assertThat(jsonResponse.size(), is(6));
	}  
	
	@Test
	public void listQuotesBasedOnNonExistingSymbol() {
		Response response = when().get("/quotes?tickerSymbol=CS.001").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Ticker symbol does not exist."));
	}
	
	@Test
	public void listQuotesBasedOnFromAndToTimestamp() {
		Response response = when().get("/quotes?fromTimestamp=2018-08-16 10:17:20&toTimestamp=2018-08-16 10:17:30").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		assertThat(jsonResponse.size(), is(2));
	}  
	
	@Test
	public void listQuotesBasedOnInvalidFromTimestamp() {
		Response response = when().get("/quotes?fromTimestamp=2018/08/16 10:17:20&toTimestamp=2018-08-16 10:17:30").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid from timestamp. Input timestamp in yyyy-MM-dd HH:mm:ss format only."));
	}
	
	@Test
	public void listQuotesBasedOnInvalidToTimestamp() {
		Response response = when().get("/quotes?fromTimestamp=2018-08-16 10:17:20&toTimestamp=2018/08/16 10:17:30").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid to timestamp. Input timestamp in yyyy-MM-dd HH:mm:ss format only."));
	}
	
	@Test
	public void listQuotesBasedOnValidFromTimestampWithoutToTimestamp() {
		Response response = when().get("/quotes?fromTimestamp=2018-08-16 10:17:20").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid timestamp parameters. Both from and to timestamps must be provided."));
	}
	
	@Test
	public void listQuotesBasedOnValidToTimestampWithoutFromTimestamp() {
		Response response = when().get("/quotes?toTimestamp=2018-08-16 10:17:30").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid timestamp parameters. Both from and to timestamps must be provided."));
	}
	
	@Test
	public void listQuotesBasedOnToTimestampEarlierThanFromTimestamp() {
		Response response = when().get("/quotes?fromTimestamp=2018-08-16 10:17:30&toTimestamp=2018-08-16 10:17:20").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("To timestamp cannot be before From timestamp."));
	}
	
	@Test
	public void listOrdersBasedOnMultipleFilterParameters() {
		Response response = when().get("/quotes?tickerSymbol=HIJ.HK&sortSeq=desc&fromTimestamp=2018-08-16 10:17:20&toTimestamp=2018-08-16 10:17:30").then().statusCode(200).and().extract().response();
		List<Integer> jsonResponse = response.jsonPath().getList("buyOrder.orderId");
		List<Integer> answer = new ArrayList<>();
		answer.add(25);
		answer.add(23);
		assertThat(jsonResponse, is(answer));
	}
	
	@Test
	public void listOrdersSortedByTickerSymbolDescending() {
		Response response = when().get("/quotes?tickerSymbol=HIJ.HK&sortSeq=desc").then().statusCode(200).and().extract().response();
		List<Integer> jsonResponse = response.jsonPath().getList("buyOrder.orderId");
		List<Integer> answer = new ArrayList<>(Arrays.asList(new Integer[] {21,31,29,27,25,23}));
		assertThat(jsonResponse, is(answer));
	}
	
	@Test
	public void listOrdersSortedByInvalidSortSequence() {
		Response response = when().get("/quotes?sortSeq=median").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Sorting sequence only allows asc and desc."));
	}
	
}
