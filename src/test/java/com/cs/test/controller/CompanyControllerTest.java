package com.cs.test.controller;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.HashMap;
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
import com.cs.dao.CompanyRepository;
import com.cs.domain.Order;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { Csteama2018Application.class })
public class CompanyControllerTest {
	@LocalServerPort
	private int serverPort;

	@Autowired
	public CompanyRepository companyRepo;

	@Before
	public void init() {
		RestAssured.port = serverPort;
	}
	
	//Story 12 Tests
	@Test
	public void getAllCompanies() {
		Response response = when().get("/companies").then().statusCode(200).and().extract().response();
		List<String> jsonResponse = response.jsonPath().getList("tickerSymbol");
		assertThat(jsonResponse.size(), is(4));
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.get("find {it.tickerSymbol=='ABC.HK'}.name"), is("CS"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='ABC.HK'}.industry.name"), is("IT Services"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='ABC.HK'}.industry.description"), is("Services"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='DEF.HK'}.name"), is("JP"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='DEF.HK'}.industry.name"), is("IT Services"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='DEF.HK'}.industry.description"), is("Services"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='HIJ.HK'}.name"), is("DBS"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='HIJ.HK'}.industry.name"), is("IT Services"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='HIJ.HK'}.industry.description"), is("Services"));
	}
	
	@Test
	public void updateCompany3ValidParams() {
		Response response =
			when().
	    		get("/company/update?tickerSymbol=ABC.HK&newTickerSymbol=ABC.SG&name=CS Singapore&industryName=Telecommunication Services").
	    	then().
	    		statusCode(200).
	    	and().extract().response();
		
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.get("tickerSymbol"), is("ABC.SG"));
		assertThat(jsonPath.get("name"), is("CS Singapore"));
		assertThat(jsonPath.get("industry.name"), is("Telecommunication Services"));
		
		Map<String,Object> rollbackMap = new HashMap<String,Object>();
		rollbackMap.put("tickerSymbol", "ABC.HK");
		rollbackMap.put("name", "CS");
		rollbackMap.put("industryName", "IT Services");
		companyRepo.updateCompanyByTickerSymbol(rollbackMap, "ABC.SG");
	}
	
	@Test
	public void updateCompany2ValidParams() {
		Response response =
			when().
	    		get("/company/update?tickerSymbol=ABC.HK&newTickerSymbol=ABC.SG&name=CS Singapore").
	    	then().
	    		statusCode(200).
	    	and().extract().response();
		
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.get("tickerSymbol"), is("ABC.SG"));
		assertThat(jsonPath.get("name"), is("CS Singapore"));
		
		Map<String,Object> rollbackMap = new HashMap<String,Object>();
		rollbackMap.put("tickerSymbol", "ABC.HK");
		rollbackMap.put("name", "CS");
		companyRepo.updateCompanyByTickerSymbol(rollbackMap, "ABC.SG");
	}
	
	@Test
	public void updateCompany1ValidParams() {
		Response response =
			when().
	    		get("/company/update?tickerSymbol=ABC.HK&newTickerSymbol=ABC.SG").
	    	then().
	    		statusCode(200).
	    	and().extract().response();
		
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.get("tickerSymbol"), is("ABC.SG"));
		Map<String,Object> rollbackMap = new HashMap<String,Object>();
		rollbackMap.put("tickerSymbol", "ABC.HK");
		companyRepo.updateCompanyByTickerSymbol(rollbackMap, "ABC.SG");
	}
	
	@Test
	public void listFilterStartWithTickerSymbol() {
		Response response =
				when().
		    		get("/companies?filterByTickerSymbol=A").
		    	then().
		    		statusCode(200).
		    	and().extract().response();
			
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.get("find {it.tickerSymbol=='ABC.HK'}.name"), is("CS"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='ABC.HK'}.industry.name"), is("IT Services"));
	}
	
	@Test
	public void listFilterStartWithCompanyName() {
		Response response =
				when().
		    		get("/companies?filterByCompanyName=D").
		    	then().
		    		statusCode(200).
		    	and().extract().response();
			
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.get("find {it.tickerSymbol=='HIJ.HK'}.name"), is("DBS"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='HIJ.HK'}.industry.name"), is("IT Services"));
	}
	
	@Test
	public void listFilterStartWithCompanyNameAndTickerSymbol() {
		Response response =
				when().
		    		get("/companies?filterByCompanyName=D&filterByTickerSymbol=HI").
		    	then().
		    		statusCode(200).
		    	and().extract().response();
			
		JsonPath jsonPath = new JsonPath(response.body().asString());
		assertThat(jsonPath.get("find {it.tickerSymbol=='HIJ.HK'}.name"), is("DBS"));
		assertThat(jsonPath.get("find {it.tickerSymbol=='HIJ.HK'}.industry.name"), is("IT Services"));
	}
}
