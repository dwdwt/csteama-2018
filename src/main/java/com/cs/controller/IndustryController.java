package com.cs.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.domain.Company;
import com.cs.domain.Industry;
import com.cs.exception.InvalidParameterException;
import com.cs.service.IndustryService;

@RestController
public class IndustryController {
	
	@Autowired
	IndustryService industrySvc;
	
	@RequestMapping("/industry")
	public Map<Industry, List<Company>> findIndustriesInformation(
				@RequestParam(value="industry", defaultValue="")String industryName) {
		Map<Industry, List<Company>> toReturn = null;
		try {
			toReturn = industrySvc.findIndustriesInformation(industryName);
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidParameterException("Industry Name does not exist.");
		}
		
		return toReturn;
	}
	
	@PostMapping("/industry/update")
	public Map<Industry, List<Company>> updateIndustryInformation(
			@RequestBody Industry industry) {
		try {
			industrySvc.updateIndustryInformation(industry);
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidParameterException("Industry Name does not exist.");
		}
		return industrySvc.findIndustriesInformation("");
	}
	
	@RequestMapping("/industry/delete/{industryName}")
	public Map<Industry, List<Company>> deleteIndustry(@PathVariable(value="industryName")String industryName) {
		industrySvc.deleteIndustry(industryName);
		return industrySvc.findIndustriesInformation("");
	}
	
}
