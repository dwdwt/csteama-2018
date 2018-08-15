package com.cs.test.controller;


import com.cs.Csteama2018Application;
import com.cs.domain.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Csteama2018Application.class})
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int serverPort;

    @Before
    public void init() {
        RestAssured.port = serverPort;
    }

    @Test
    public void canGetAllUsers() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/users").
                then().
                statusCode(SC_OK).body(
                "[0].id", equalTo(1),
                "[0].firstName", equalTo("Jon"),
                "[0].lastName", equalTo("Doe"),
                "[0].contact", equalTo("1234"),
                "[0].email", equalTo("jondoe@gmail.com"),
                "[0].role", equalTo("TRADER"),
                "[0].address", equalTo("smu"),
                "[1].id", equalTo(2),
                "[2].id", equalTo(3));
    }

    @Test
    public void canGetUserById() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/user/1").
                then().
                statusCode(SC_OK).body(
                "id", equalTo(1),
                "firstName", equalTo("Jon"),
                "lastName", equalTo("Doe"),
                "contact", equalTo("1234"),
                "email", equalTo("jondoe@gmail.com"),
                "role", equalTo("TRADER"),
                "address", equalTo("smu"));
    }

    @Test
    public void canGetUserOtherThanTrader() {
        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/users").
                then().
                statusCode(SC_OK).body(
                "find {it.firstName == 'Brandon'}.role", equalTo("QA"));
    }

    @Test
    public void canAddANewTrader() {
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("firstName", "someFirstName");
        userInfo.put("lastName", "someLastName");
        userInfo.put("contact", "someContact");
        userInfo.put("email", "someEmail");
        userInfo.put("role", "TRADER");
        userInfo.put("address", "someAddress");

        given()
                .contentType("application/json")
                .body(userInfo)
                .when().post("/users").then()
                .statusCode(200);

        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/users").
                then().
                statusCode(SC_OK).body("find {it.firstName == 'someFirstName'}.lastName",equalTo("someLastName"),
                "find {it.firstName == 'someFirstName'}.email",equalTo("someEmail"));
    }

    @Test
    public void canDeleteTraderByIdWithoutPosition() {
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("firstName", "someTraderToBeDeleted");
        userInfo.put("lastName", "someLastName");
        userInfo.put("contact", "someContact");
        userInfo.put("email", "someEmail");
        userInfo.put("role", "TRADER");
        userInfo.put("address", "someAddress");

        //insert trader data
        given()
                .contentType("application/json")
                .body(userInfo)
                .when().post("/users").then()
                .statusCode(200);

        //check traderid
        Response response = given()
                .contentType("application/json")
                .body(userInfo)
                .when().get("/users").then().
                        contentType(ContentType.JSON).extract().response();

        List<HashMap<String, Object>> mapList = response.jsonPath().getList("$");

        int latestId = (Integer) mapList.get(mapList.size() -1).get("id");

        //delete trader data

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/user/" + latestId).then()
                .statusCode(200);

        //check trader data do not exist

        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/users").
                then().
                statusCode(SC_OK).body("find {it.firstName == 'someTraderToBeDeleted'}.lastName",equalTo(null),
                "find {it.firstName == 'someTraderToBeDeleted'}.email",equalTo(null));
    }

    @Test
    public void cannotDeleteTraderById() {

        //bad request when delete jon doe

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/user/" + 1).then()
                .statusCode(SC_BAD_REQUEST).body("message", equalTo("This trader has orders in any status"));

        //check trader data still exist

        given().
                accept(MediaType.APPLICATION_JSON_VALUE).when().get("/user/" + 1).
                then().
                statusCode(SC_OK).body("lastName",equalTo("Doe"),
                "email",equalTo("jondoe@gmail.com"));
    }
}
