package com.cs.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dao.CompanyRepository;
import com.cs.domain.Company;

@Service
public class CompanyService {
	
	@Autowired
	CompanyRepository companyRepo;
	
	public List<Company> findAllCompanies(Map<String,String> filterMap){
        return companyRepo.findAllCompanies(filterMap);
    }
	
	public Company findCompanyByTickerSymbol(String tickerSymbol){
        return companyRepo.findCompanyByTickerSymbol(tickerSymbol);
    }
	
	public Company updateCompanyByMapAndTickerSymbol(Map<String,Object> updateMap, String tickerSymbol) {
		return companyRepo.updateCompanyByTickerSymbol(updateMap,tickerSymbol);
		
	}
	
	public void deleteCompany(Company company) {
		companyRepo.deleteCompany(company);
	}
}
