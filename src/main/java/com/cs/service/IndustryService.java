package com.cs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.dao.CompanyRepository;
import com.cs.dao.IndustryRepository;
import com.cs.domain.Company;
import com.cs.domain.Industry;
import com.cs.exception.InvalidActionException;

@Service
public class IndustryService {
	
	@Autowired
	CompanyRepository companyRepo;
	
	@Autowired
	IndustryRepository industryRepo;
	
	public Map<Industry, List<Company>> findIndustriesInformation(String industryName){
		Map<Industry, List<Company>> toReturn = new HashMap<>();
        List<Industry> industries = new ArrayList<>();
        if(industryName.isEmpty()) {
        	industries = industryRepo.findAllIndustries();
        }else {
        	industries.add(industryRepo.findIndustryByName(industryName));
        }
        for(Industry industry: industries) {
        	List<Company> companies = companyRepo.findCompaniesByIndustry(industry.getName());
        	toReturn.put(industry, companies);
        }
        return toReturn;
    }
	
	public void updateIndustryInformation(Industry industry) {
		industryRepo.updateIndustryInformation(industry);
	}
	
	public void deleteIndustry(String industryName) {
		List<Company> companies = companyRepo.findCompaniesByIndustry(industryName);
		if(companies.isEmpty()) {
			industryRepo.deleteIndustry(industryName);
		}else {
			throw new InvalidActionException("Valid stocks exist under industry name. Unable to delete industry.");
		}
		
	}
}
