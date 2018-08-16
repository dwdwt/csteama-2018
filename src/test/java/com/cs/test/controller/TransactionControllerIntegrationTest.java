package com.cs.test.controller;


import com.cs.Csteama2018Application;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Csteama2018Application.class})
public class TransactionControllerIntegrationTest {

    @LocalServerPort
    private int serverPort;

    @Before
    public void init() {
        RestAssured.port = serverPort;
    }

    @Test
    public void canViewAllTransactionsInDescOrderForTimeStamp() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/transactions").
                then().
                statusCode(SC_OK).body("[0].txnId", equalTo(3),
                "[0].userId", equalTo(2),
                "[0].operation", equalTo("CANCEL"),
                "[0].timeStamp", equalTo("2012-11-11T13:23:44.000+08:00"),
                "[0].stockSymbol", equalTo("HIJ.HK"),
                "[0].quantity", equalTo(7),
                "[0].askPrice", equalTo(10.2f),
                "[0].typeOfOrder", equalTo("LIMIT"),
                "[1].txnId", equalTo(2),
                "[2].txnId", equalTo(1));
    }

    @Test
    public void canViewTransactionsByUserAndSymbolCriteria() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/transactions?userId=2&stockSymbol=DEF.HK").
                then().
                statusCode(SC_OK).body("[0].txnId", equalTo(2),
                "[0].userId", equalTo(2),
                "[0].operation", equalTo("FILL"),
                "[0].timeStamp", equalTo("2010-11-11T13:23:44.000+08:00"),
                "[0].stockSymbol", equalTo("DEF.HK"),
                "[0].quantity", equalTo(6),
                "[0].askPrice", equalTo(10.1f),
                "[0].typeOfOrder", equalTo("MARKET"),
                "[1]", equalTo(null),
                "[2]", equalTo(null));
    }

    @Test
    public void canViewTransactionsByTimeStampCriteriaWithFilter() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/transactions?fromDate=2010-11-11&toDate=2012-11-12").
                then().
                statusCode(SC_OK).body("[0].txnId", equalTo(3),

                "[1].txnId", equalTo(2),
                "[1].userId", equalTo(2),
                "[1].operation", equalTo("FILL"),
                "[1].timeStamp", equalTo("2010-11-11T13:23:44.000+08:00"),
                "[1].stockSymbol", equalTo("DEF.HK"),
                "[1].quantity", equalTo(6),
                "[1].askPrice", equalTo(10.1f),
                "[1].typeOfOrder", equalTo("MARKET"),

                "[2]", equalTo(null));
    }

    @Test
    public void canViewTransactionsByUserAndSymbolCriteriaByFilter() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/transactions?userId=2&stockSymbol=DEF.HK&filter=txnId,userId,quantity").
                then().
                statusCode(SC_OK).body("[0].txnId", equalTo(2),
                "[0].userId", equalTo(2),
                "[0].operation", equalTo(null),
                "[0].timeStamp", equalTo(null),
                "[0].stockSymbol", equalTo(null),
                "[0].quantity", equalTo(6),
                "[0].askPrice", equalTo(null),
                "[0].typeOfOrder", equalTo(null),
                "[1]", equalTo(null),
                "[2]", equalTo(null));
    }

    @Test
    public void willHaveServerErrorWhenArgumentIsNotGood() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/transactions?userId=2&stockSymbol=DEF.HK&filter=txnId,rubbish").
                then().
                statusCode(SC_BAD_REQUEST).body("message", equalTo("No Such Filter: rubbish"));
    }

}
