package com.cs.test.controller;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { Csteama2018Application.class })
public class OrderControllerTest {
	@LocalServerPort
	private int serverPort;

	@Before
	public void init() {
		RestAssured.port = serverPort;
	}

	// Story 2 Tests
	@Test
	public void cancelOpenOrder() {
		Response response = when().get("/cancel/{orderId}", 1).then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("status");
		String cancelledOrder = jsonResponse.get(jsonResponse.size() - 1);
		assertThat(cancelledOrder, is("CANCELLED"));
	}

	// Story 3 Tests
	@Test
	public void updateExistingOrderWithoutParameters() {
		Response response = when().get("/update/1").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("No update parameters found."));
	}
	
	@Test
	public void updateExistingOrderWithValidParameters() {
		Response response = when().
				get("/update/1?quantity=30&price=50.0&orderType=MARKET").
				then().
					statusCode(200).
				and().
				extract().response();
		
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.get("find {it.orderId==1}.noOfShares"), is(30));
		assertThat(jsonPath.get("find {it.orderId==1}.price"), is((float)50.0));
		assertThat(jsonPath.get("find {it.orderId==1}.type"), is("MARKET"));
	}

	@Test
	public void updateNonExistingOrderWithParameters() {
		Response response = when().get("/update/900?quantity=30").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid order id."));
	}

	@Test
	public void updateExistingOrderWithInvalidQuantityParameter() {
		Response response = when().get("/update/1?quantity=thirty").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Number of shares has to be an integer value."));
	}

	@Test
	public void updateExistingOrderWithInvalidPriceParameter() {
		Response response = when().get("/update/1?price=fifty").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Price has to be a double value."));
	}

	@Test
	public void updateExistingOrderWithInvalidOrderTypeParameter() {
		Response response = when().get("/update/1?orderType=Market").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Order type has to be either MARKET or LIMIT."));
	}

	// Story 4 Tests
	@Test
	public void listAllOrders() {
		Response response = when().get("/orders").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		assertThat(jsonResponse.size(), is(8));
	}
	
	@Test
	public void listOrdersBasedOnSide() {
		Response response = when().get("/orders?side=B").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		assertThat(jsonResponse.size(), is(4));
	}
	
	@Test
	public void listOrdersBasedOnInvalidSide() {
		Response response = when().get("/orders?side=Buy").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid side parameter. Input only B or S."));
	}
	
	@Test
	public void listOrdersBasedOnOrderType() {
		Response response = when().get("/orders?orderType=MARKET").then().statusCode(200).and().extract().response();
		List<Integer> jsonResponse = response.jsonPath().getList("orderId");
		//1 was updated in the method updateExistingOrderWithInvalidOrderTypeParameter() above
		assertThat(jsonResponse.size(), is(3));
	}
	
	@Test
	public void listOrdersBasedOnInvalidOrderType() {
		Response response = when().get("/orders?orderType=Market").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid order type parameter. Input only MARKET or LIMIT."));
	}
	
	@Test
	public void listOrdersBasedOnStatus() {
		Response response = when().get("/orders?status=OPENED").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		assertThat(jsonResponse.size(), is(2));
	}
	
	@Test
	public void listOrdersBasedOnInvalidStatus() {
		Response response = when().get("/orders?status=PARTIALLY_FILLED").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid order status parameter. Input only OPENED, FILLED or CANCELLED."));
	}
	
	@Test
	public void listOrdersBasedOnSymbol() {
		Response response = when().get("/orders?tickerSymbol=HIJ.HK").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		assertThat(jsonResponse.size(), is(6));
	}  
	
	@Test
	public void listOrdersBasedOnNonExistingSymbol() {
		Response response = when().get("/orders?tickerSymbol=CS.001").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Ticker symbol does not exist."));
	}
	
	@Test
	public void listOrdersBasedOnFromAndToQuantity() {
		Response response = when().get("/orders?fromOrderQty=0&toOrderQty=10").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		//1 was updated in the method updateExistingOrderWithValidParameters() above
		assertThat(jsonResponse.size(), is(1));
	}  
	
	@Test
	public void listOrdersBasedOnInvalidFromQuantity() {
		Response response = when().get("/orders?fromOrderQty=zero&toOrderQty=10").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid from order quantity. Input only integers."));
	}
	
	@Test
	public void listOrdersBasedOnInvalidToQuantity() {
		Response response = when().get("/orders?fromOrderQty=0&toOrderQty=ten").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid to order quantity. Input only integers."));
	}
	
	@Test
	public void listOrdersBasedOnValidFromQuantityWithoutToQuantity() {
		Response response = when().get("/orders?fromOrderQty=0").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid order quantity parameters. Both from and to order quantities must be provided."));
	}
	
	@Test
	public void listOrdersBasedOnValidToQuantityWithoutFromQuantity() {
		Response response = when().get("/orders?toOrderQty=10").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid order quantity parameters. Both from and to order quantities must be provided."));
	}
	
	@Test
	public void listOrdersBasedOnToQuantitySmallerThanFromQuantity() {
		Response response = when().get("/orders?fromOrderQty=20&toOrderQty=10").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("To order quantity cannot be smaller than From order quantity."));
	}
	
	@Test
	public void listOrdersBasedOnFromAndToTimestamp() {
		Response response = when().get("/orders?fromTimestamp=2018-08-16 10:17:20&toTimestamp=2018-08-16 10:17:30").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		assertThat(jsonResponse.size(), is(3));
	}  
	
	@Test
	public void listOrdersBasedOnInvalidFromTimestamp() {
		Response response = when().get("/orders?fromTimestamp=2018/08/16 10:17:20&toTimestamp=2018-08-16 10:17:30").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid from timestamp. Input timestamp in yyyy-MM-dd HH:mm:ss format only."));
	}
	
	@Test
	public void listOrdersBasedOnInvalidToTimestamp() {
		Response response = when().get("/orders?fromTimestamp=2018-08-16 10:17:20&toTimestamp=2018/08/16 10:17:30").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid to timestamp. Input timestamp in yyyy-MM-dd HH:mm:ss format only."));
	}
	
	@Test
	public void listOrdersBasedOnValidFromTimestampWithoutToTimestamp() {
		Response response = when().get("/orders?fromTimestamp=2018-08-16 10:17:20").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid timestamp parameters. Both from and to timestamps must be provided."));
	}
	
	@Test
	public void listOrdersBasedOnValidToTimestampWithoutFromTimestamp() {
		Response response = when().get("/orders?toTimestamp=2018-08-16 10:17:30").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid timestamp parameters. Both from and to timestamps must be provided."));
	}
	
	@Test
	public void listOrdersBasedOnToTimestampEarlierThanFromTimestamp() {
		Response response = when().get("/orders?fromTimestamp=2018-08-16 10:17:30&toTimestamp=2018-08-16 10:17:20").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("To timestamp cannot be before From timestamp."));
	}
	
	@Test
	public void listOrdersBasedOnMultipleFilterParameters() {
		Response response = when().get("/orders?side=B&orderType=MARKET").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("orderId");
		//1 was updated in the method updateExistingOrderWithValidParameters() above
		assertThat(jsonResponse.size(), is(2));
	}
	
	@Test
	public void listOrdersSortedByTickerSymbolAscending() {
		Response response = when().get("/orders?sort=tickerSymbol&sortSeq=asc").then().statusCode(200).and().extract().response();
		List<Integer> jsonResponse = response.jsonPath().getList("orderId");
		List<Integer> answer = new ArrayList<>();
		answer.add(1);
		answer.add(2);
		answer.add(3);
		answer.add(4);
		answer.add(5);
		answer.add(6);
		answer.add(7);
		answer.add(8);
		
		assertThat(jsonResponse, is(answer));
	}
	
	@Test
	public void listOrdersSortedByTickerSymbolDescending() {
		Response response = when().get("/orders?sort=tickerSymbol&sortSeq=desc").then().statusCode(200).and().extract().response();
		List<Integer> jsonResponse = response.jsonPath().getList("orderId");
		List<Integer> answer = new ArrayList<>();
		answer.add(3);
		answer.add(4);
		answer.add(5);
		answer.add(6);
		answer.add(7);
		answer.add(8);
		answer.add(2);
		answer.add(1);
		
		assertThat(jsonResponse, is(answer));
	}
	
	@Test
	public void listOrdersSortedByPriceAscending() {
		Response response = when().get("/orders?sort=price&sortSeq=asc").then().statusCode(200).and().extract().response();
		List<Integer> jsonResponse = response.jsonPath().getList("orderId");
		List<Integer> answer = new ArrayList<>();
		answer.add(1);
		answer.add(2);
		answer.add(3);
		answer.add(4);
		answer.add(5);
		answer.add(6);
		answer.add(7);
		answer.add(8);
		
		assertThat(jsonResponse, is(answer));
	}
	
	@Test
	public void listOrdersSortedByPriceDesscending() {
		Response response = when().get("/orders?sort=price&sortSeq=desc").then().statusCode(200).and().extract().response();
		List<Integer> jsonResponse = response.jsonPath().getList("orderId");
		List<Integer> answer = new ArrayList<>();
		answer.add(1);
		answer.add(2);
		answer.add(3);
		answer.add(4);
		answer.add(5);
		answer.add(6);
		answer.add(7);
		answer.add(8);
		
		assertThat(jsonResponse, is(answer));
	}
	
	@Test
	public void listOrdersSortedByInvalidSortParamValidSortSequence() {
		Response response = when().get("/orders?sort=noOfShares&sortSeq=desc").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Sorting parameters only include tickerSymbol and price."));
	}
	
	@Test
	public void listOrdersSortedByValidSortParamInvalidSortSequence() {
		Response response = when().get("/orders?sort=price&sortSeq=median").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Sorting sequence only allows asc and desc."));
	}
	
	@Test
	public void listOrdersSortedByValidSortParamWithoutSortSequence() {
		Response response = when().get("/orders?sort=price").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid sorting parameters. Both sort and sortSeq must be provided."));
	}
	
	@Test
	public void listOrdersSortedByValidSortSequenceParamWithoutSortParam() {
		Response response = when().get("/orders?sortSeq=asc").then().statusCode(400).and().extract().response();
		String message = response.jsonPath().getString("message");
		assertThat(message, is("Invalid sorting parameters. Both sort and sortSeq must be provided."));
	}
}
