package com.cs.test.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Csteama2018Application;
import com.cs.dao.IndustryRepository;
import com.cs.domain.Industry;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { Csteama2018Application.class })
public class IndustryControllerIntegrationTest {
	@LocalServerPort
	private int serverPort;
	
	@Autowired
	IndustryRepository industryRepo;

	@Before
	public void init() {
		RestAssured.port = serverPort;
	}

	// Story 13 Tests
	@Test
	public void findAllIndustriesInformationWithoutParam() {
		Response response = 
				when().
					get("/industry").
				then().
					statusCode(200).
				and().
					extract().response();
		
		JsonPath jsonPath = new JsonPath(response.body().asString());
		
		Map<String, List<String>> industryNames = response.jsonPath().getMap("findAll {it}");
		int counter = 0;
		List<Integer> answer = new ArrayList<Integer>(Arrays.asList(new Integer[] {0,0,3}));
		for(String name: industryNames.keySet()) {
			int data = 0;
			List<String> dataList = jsonPath.getList("'" + name + "'.tickerSymbol");
			if(dataList != null) data = dataList.size();
			assertThat(data, is(answer.get(counter)));
			counter++;
		}
	}  
	
	@Test
	public void findAllIndustriesInformationWithTickerSymbol() {
		Response response = 
				when().
					get("/industry?industry=IT Services").
				then().
					statusCode(200).
				and().
					extract().response();
		
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.getList("'IT Services'.tickerSymbol").size(), is(3));
	}
	
	@Test
	public void updateIndustryWithValidBody() {
		Industry industry = new Industry("IT Services", "Technology Services");
		try {
			Response response =
			    	given()
			    		.contentType("application/json")
			    		.body(industry)
			    	.when()
			    		.post("/industry/update")
			    	.then()
			    		.statusCode(200)
			    	.and()	
			    		.extract().response();
			
			JsonPath jsonPath = new JsonPath(response.body().asString());
			assertThat(jsonPath.getList("'IT Services'.industry.description").get(0), is("Technology Services"));
		} finally {
			industry.setDescription("Services");
			industryRepo.updateIndustryInformation(industry);
		}
	}
	
	@Test
	public void deleteIndustryWithValidTickerSymbol() {
		industryRepo.insertIndustry("insert into industries(name,description) values ('Testing Services', 'Services')");
    	
		Response response = 
				when().
					get("/industry/delete/Testing Services").
				then().
					statusCode(200).
				and().
					extract().response();
		
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.getMap("findAll {it}").keySet().size(), is(3));
	}
	
	@Test
	public void deleteIndustryWithInvalidTickerSymbol() {
		when().
			get("/industry/delete/IT Services").
		then().
			statusCode(400).body("message", equalTo("Valid stocks exist under industry name. Unable to delete industry."));
	}
}
