package com.cs.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.dao.IndustryRepository;
import com.cs.dao.OrderRepository;
import com.cs.domain.Company;
import com.cs.domain.Industry;
import com.cs.domain.Order;
import com.cs.exception.InvalidActionException;
import com.cs.exception.InvalidParameterException;
import com.cs.service.CompanyService;



@RestController
public class CompanyController {
	@Autowired
	CompanyService companySvc;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	IndustryRepository industryRepo;
	
	@RequestMapping("/companies")
	public List<Company> findAllCompanies(@RequestParam(value = "filterByTickerSymbol", defaultValue = "") String filterByTickerSymbol,
            @RequestParam(value = "filterByCompanyName", defaultValue = "") String filterByCompanyName){
		Map<String, String> filterMap = new HashMap<String,String>();
		if (!filterByTickerSymbol.isEmpty()) filterMap.put("filterByTickerSymbol", filterByTickerSymbol);
		if (!filterByCompanyName.isEmpty()) filterMap.put("filterByCompanyName", filterByCompanyName);
		return companySvc.findAllCompanies(filterMap);
	}
	
	@RequestMapping("/company/update")
	public Company updateCompany(@RequestParam("tickerSymbol") String tickerSymbol,
			@RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "industryName", defaultValue = "") String industryName,
            @RequestParam(value = "newTickerSymbol", defaultValue = "") String newTickerSymbol){
		
		
		if (tickerSymbol.isEmpty() || (name.isEmpty() && industryName.isEmpty() && newTickerSymbol.isEmpty())) {
            throw new InvalidParameterException("No update parameters found.");
        }
		
		try {
			if (!industryName.isEmpty()) {
				Industry industry = industryRepo.findIndustryByName(industryName);
			}
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidParameterException("Invalid industry/market sector.");
		}
		
		try {
			Company companyToBeUpdated = companySvc.findCompanyByTickerSymbol(tickerSymbol);
			Map<String, Object> updateMap = new HashMap<>();
			if (!newTickerSymbol.isEmpty()) updateMap.put("tickerSymbol", newTickerSymbol);
			if (!name.isEmpty()) updateMap.put("name", name);
	        if (!industryName.isEmpty()) updateMap.put("industryName", industryName);
	        
	        
	        return companySvc.updateCompanyByMapAndTickerSymbol(updateMap, tickerSymbol);
		}catch(EmptyResultDataAccessException e) {
			throw new InvalidParameterException("Invalid ticker symbol.");
		}
	}
	
	@RequestMapping("/company/delete/{tickerSymbol}")
	public List<Company> deleteCompany(@PathVariable("tickerSymbol") String tickerSymbol) {
		if (tickerSymbol.isEmpty()) {
            throw new InvalidParameterException("No delete parameters found.");
        }
		try {
			Company companyToBeDeleted = companySvc.findCompanyByTickerSymbol(tickerSymbol);
			Map<String,String> tickerSymbolMap = new HashMap<String,String>();
			tickerSymbolMap.put("tickerSymbol", tickerSymbol);
			List<Order> orderListOfCompany = orderRepo.filterAndSortOrdersByCriteria(tickerSymbolMap, "", "");
			if(orderListOfCompany.size() > 0) throw new InvalidActionException("Cannot delete company as it has orders");
			companySvc.deleteCompany(companyToBeDeleted);
			
		}catch(EmptyResultDataAccessException e) {
			throw new InvalidParameterException("Invalid ticker symbol.");
		}
		Map<String, String> filterMap = new HashMap<String,String>();
		return companySvc.findAllCompanies(filterMap);
	}
}
