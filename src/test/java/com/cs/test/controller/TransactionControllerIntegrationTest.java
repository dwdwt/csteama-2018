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
    public void canViewAllTransactions() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/transactions").
                then().
                statusCode(SC_OK).body("[0].txnId", equalTo(1),
                "[0].userId", equalTo(1),
                "[0].operation", equalTo("OPEN"),
                "[0].timeStamp", equalTo("2008-11-11T13:23:44.000-05:00"),
                "[0].stockSymbol", equalTo("ABC.HK"),
                "[0].quantity", equalTo(5),
                "[0].askPrice", equalTo(10.0f),
                "[0].typeOfOrder", equalTo("LIMIT"),
                "[1].txnId", equalTo(2),
                "[2].txnId", equalTo(3));
    }

    @Test
    public void canViewTransactionsByUserAndSymbolCriteria() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/transactions?userId=2&stockSymbol=DEF.HK").
                then().
                statusCode(SC_OK).body("[0].txnId", equalTo(2),
                "[0].userId", equalTo(2),
                "[0].operation", equalTo("FILL"),
                "[0].timeStamp", equalTo("2010-11-11T13:23:44.000-05:00"),
                "[0].stockSymbol", equalTo("DEF.HK"),
                "[0].quantity", equalTo(6),
                "[0].askPrice", equalTo(10.1f),
                "[0].typeOfOrder", equalTo("MARKET"),
                "[1]", equalTo(null),
                "[2]", equalTo(null));
    }

    @Test
    public void canViewTransactionsByTimeStampCriteria() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/transactions?fromDate=2010-11-11&toDate=2012-11-12").
                then().
                statusCode(SC_OK).body("[0].txnId", equalTo(2),
                "[0].userId", equalTo(2),
                "[0].operation", equalTo("FILL"),
                "[0].timeStamp", equalTo("2010-11-11T13:23:44.000-05:00"),
                "[0].stockSymbol", equalTo("DEF.HK"),
                "[0].quantity", equalTo(6),
                "[0].askPrice", equalTo(10.1f),
                "[0].typeOfOrder", equalTo("MARKET"),
                "[1].txnId", equalTo(3),
                "[2]", equalTo(null));
    }



}
