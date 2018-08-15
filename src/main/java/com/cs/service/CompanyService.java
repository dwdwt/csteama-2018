package com.cs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dao.CompanyRepository;
import com.cs.domain.Company;

@Service
public class CompanyService {
	
	@Autowired
	CompanyRepository companyRepo;
	
	public List<Company> findAllCompanies(){
        return companyRepo.findAllCompanies();
    }
	
	public Company findCompanyByTickerSymbol(String tickerSymbol){
        return companyRepo.findCompanyByTickerSymbol(tickerSymbol);
    }
}
